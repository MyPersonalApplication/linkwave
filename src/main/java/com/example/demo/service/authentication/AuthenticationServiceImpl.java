package com.example.demo.service.authentication;

import com.example.demo.controller.exception.AuthenticationException;
import com.example.demo.controller.exception.ConflictingDataException;
import com.example.demo.dto.authentication.AuthenticationResponse;
import com.example.demo.dto.authentication.RegisterDTO;
import com.example.demo.dto.user.UserDTO;
import com.example.demo.enums.ErrorMessage;
import com.example.demo.model.User;
import com.example.demo.service.keycloak.KeycloakService;
import com.example.demo.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
    private final KeycloakService keycloakService;
    private final UserService userService;
    private final String realmName = "itlinkwave";

    @Override
    public AuthenticationResponse authenticate(String username, String password) throws AccessDeniedException {
        try {
            AuthenticationResponse responseDTO = new AuthenticationResponse();
            Keycloak newKeycloak = keycloakService.newKeycloakBuilderWithPasswordCredentials(username, password, realmName).build();

            responseDTO.setAccessTokenResponse(newKeycloak.tokenManager().getAccessToken());

            UsersResource usersResource = keycloakService.getRealmResource(realmName).users();
            Optional<UserRepresentation> userRepresentation = usersResource.search(username).stream().findFirst();
            if (userRepresentation.isEmpty()) {
                throw new AuthenticationException(ErrorMessage.COULD_NOT_FOUND_USER);
            }
            UserResource userResource = usersResource.get(userRepresentation.get().getId());
            List<String> roles = userResource.roles().realmLevel().listEffective().stream()
                    .map(RoleRepresentation::getName).map(role -> "ROLE_" + role).toList();
            responseDTO.setRoles(roles);

            return responseDTO;
        } catch (AccessDeniedException ex){
            throw ex;
        } catch (Exception ex) {
            throw new AuthenticationException(ErrorMessage.INCORRECT_LOGIN_CREDENTIALS, ex);
        }
    }

    @Override
    public String registerNewUser(RegisterDTO registerDTO) {
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

                try {
                    User userEntity = User.builder()
                            .id(UUID.fromString(userId))
                            .email(registerDTO.getEmail())
                            .isActive(true)
                            .createdOn(new Date())
                            .build();

                    userService.save(userEntity);
                    return "User registered successfully!";
                } catch (Exception e) {
                    log.error("Error while saving user: {}", e.getMessage());
                    deleteAccountInKeycloak(userId, realmResource);
                    throw new ConflictingDataException("Can not create user");
                }
            } else {
                return "Failed to register user. HTTP response code: " + response.getStatus();
            }
        } catch (Exception e) {
            return "Failed to register user: " + e.getMessage();
        }
    }

    @Override
    public UserDTO getProfile() {
        return keycloakService.getUserProfile(realmName);
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
