package com.example.demo.service.authentication;

import com.example.demo.controller.exception.AuthenticationException;
import com.example.demo.controller.exception.ConflictingDataException;
import com.example.demo.controller.exception.NotFoundException;
import com.example.demo.dto.ResponseDTO;
import com.example.demo.dto.authentication.AuthenticationResponse;
import com.example.demo.dto.authentication.RegisterDTO;
import com.example.demo.dto.user.UserCreateDTO;
import com.example.demo.dto.user.UserDTO;
import com.example.demo.enums.CredentialResetAction;
import com.example.demo.enums.ErrorMessage;
import com.example.demo.mapper.user.UserMapper;
import com.example.demo.model.user.User;
import com.example.demo.model.user.UserAvatar;
import com.example.demo.model.user.UserCover;
import com.example.demo.model.user.UserProfile;
import com.example.demo.repository.user.UserRepository;
import com.example.demo.service.keycloak.KeycloakService;
import com.example.demo.service.user.UserService;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
    private final KeycloakService keycloakService;
    private final UserRepository userRepository;
    private final UserService userService;
    private final String realmName = "linkwave";

    @Override
    public AuthenticationResponse authenticate(String username, String password) throws AccessDeniedException {
        try {
            keycloakService.isVerifiedEmail(username, realmName);
            Keycloak newKeycloak = keycloakService.newKeycloakBuilderWithPasswordCredentials(username, password, realmName).build();
            AccessTokenResponse accessTokenResponse = newKeycloak.tokenManager().getAccessToken();

            UsersResource usersResource = keycloakService.getRealmResource(realmName).users();
            Optional<UserRepresentation> userRepresentation = usersResource.search(username).stream().findFirst();
            if (userRepresentation.isEmpty()) {
                throw new AuthenticationException(ErrorMessage.COULD_NOT_FOUND_USER);
            }
            UserResource userResource = usersResource.get(userRepresentation.get().getId());
            List<String> roles = userResource.roles().realmLevel().listEffective().stream()
                    .map(RoleRepresentation::getName).map(role -> "ROLE_" + role).toList();

            UserDTO userDTO = userService.getProfileByUserId(userRepresentation.get().getId());

            return AuthenticationResponse.builder()
                    .accessTokenResponse(accessTokenResponse)
                    .roles(roles)
                    .user(userDTO)
                    .build();
        } catch (AccessDeniedException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new AuthenticationException(ErrorMessage.INCORRECT_LOGIN_CREDENTIALS, ex);
        }
    }

    @Override
    public ResponseDTO registerNewUser(RegisterDTO registerDTO) {
        try {
            // Assuming keycloakService is an instance of your Keycloak service
            Keycloak keycloak = keycloakService.getMasterKeycloakInstance().build();
            RealmResource realmResource = keycloak.realm(realmName);

            UserRepresentation user = getUserRepresentation(registerDTO);

            // Create the user
            Response response = realmResource.users().create(user);
            if (response.getStatus() == 201) {
                String userId = CreatedResponseUtil.getCreatedId(response);
                RoleRepresentation userRole = realmResource.roles().get("USER").toRepresentation();
                realmResource.users().get(userId).roles().realmLevel().add(Collections.singletonList(userRole));
                realmResource.users().get(userId).sendVerifyEmail();

                try {
                    UserCreateDTO userCreateDTO = UserCreateDTO.builder()
                            .id(UUID.fromString(userId))
                            .email(registerDTO.getEmail())
                            .isActive(true)
                            .build();

                    User userEntity = UserMapper.INSTANCE.toEntity(userCreateDTO);
                    userEntity.setCreatedAt(new Date());
                    userEntity.setUpdatedAt(new Date());

                    UserProfile userProfile = UserProfile.builder()
                            .createdAt(new Date())
                            .updatedAt(new Date())
                            .user(userEntity)
                            .build();

                    UserAvatar userAvatar = UserAvatar.builder()
                            .createdAt(new Date())
                            .updatedAt(new Date())
                            .user(userEntity)
                            .build();

                    UserCover userCover = UserCover.builder()
                            .createdAt(new Date())
                            .updatedAt(new Date())
                            .user(userEntity)
                            .build();

                    userEntity.setUserProfile(userProfile);
                    userEntity.setUserAvatar(userAvatar);
                    userEntity.setUserCover(userCover);

                    userRepository.save(userEntity);
                    return ResponseDTO.builder()
                            .message("User registered successfully!")
                            .build();
                } catch (Exception e) {
                    log.error("Error while saving user: {}", e.getMessage());
                    deleteAccountInKeycloak(userId, realmResource);
                    throw new ConflictingDataException("Can not create user");
                }
            } else {
                throw new ConflictingDataException("Failed to register user");
            }
        } catch (Exception e) {
            throw new ConflictingDataException("Failed to register user");
        }
    }

    @Override
    public void resetPassword(String email) {
        RealmResource realmResource = keycloakService.getRealmResource(realmName);
        UsersResource usersResource = realmResource.users();
        var user = getUserRepresentationByEmail(email);
        usersResource.get(user.getId()).executeActionsEmail(
                List.of(CredentialResetAction.UPDATE_PASSWORD.name()));
    }

    @Override
    public void resendVerificationEmail(String email) {
        RealmResource realmResource = keycloakService.getRealmResource(realmName);
        var user = getUserRepresentationByEmail(email);
        UsersResource usersResource = realmResource.users();
        usersResource.get(user.getId()).sendVerifyEmail();
    }

    private UserRepresentation getUserRepresentationByEmail(String email) {
        try {
            RealmResource realmResource = keycloakService.getRealmResource(realmName);
            return realmResource.users().search(email).get(0);
        } catch (Exception ex) {
            throw new NotFoundException(ErrorMessage.USER_NOT_FOUND);
        }
    }

    private UserRepresentation getUserRepresentation(RegisterDTO registerDTO) {
        UserRepresentation user = new UserRepresentation();
        user.setUsername(registerDTO.getEmail());
        user.setFirstName(registerDTO.getFirstName());
        user.setLastName(registerDTO.getLastName());
        user.setEmail(registerDTO.getEmail());
        user.setEnabled(true);

        // Set a password for the user
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setTemporary(false);
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(registerDTO.getPassword());

        user.setCredentials(Collections.singletonList(credential));
        return user;
    }

    private void deleteAccountInKeycloak(String userId, RealmResource realmResource) {
        UsersResource usersResource = realmResource.users();
        usersResource.delete(userId);
    }
}
