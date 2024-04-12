package com.example.demo.service.keycloak;

import com.example.demo.config.authentication.TokenHandler;
import com.example.demo.controller.exception.NotFoundException;
import com.example.demo.dto.friendrequest.RecommendDTO;
import com.example.demo.dto.friendrequest.UserRecommendDTO;
import com.example.demo.dto.user.UserDTO;
import com.example.demo.enums.ErrorMessage;
import com.example.demo.model.friend.Friendship;
import com.example.demo.model.user.User;
import com.example.demo.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class KeycloakService {
    @Value("${keycloak.realm}")
    private String keycloakRealm;
    @Value("${keycloak.url}")
    private String keycloakUrl;
    @Value("${keycloak.client-id}")
    private String keycloakClientId;
    @Value("${keycloak.master-client-id}")
    private String masterKeycloakClientId;
    @Value("${keycloak.username}")
    private String keycloakUsername;
    @Value("${keycloak.password}")
    private String keycloakPassword;

    private final TokenHandler tokenHandler;
    private final Keycloak keycloak;
    private final UserRepository userRepository;

    public KeycloakBuilder newKeycloakBuilderWithPasswordCredentials(String username, String password, String realm) {
        return KeycloakBuilder.builder()
                .realm(realm)
                .serverUrl(keycloakUrl)
                .clientId(keycloakClientId)
                .username(username)
                .password(password);
    }

    public void isVerifiedEmail(String email, String realm) {
        RealmResource realmResource = keycloak.realm(realm);
        var user = realmResource.users().search(email).get(0);
        if (Boolean.FALSE.equals(user.isEmailVerified())) {
            throw new AccessDeniedException(ErrorMessage.HAVE_NOT_VERIFIED_EMAIL);
        }
    }

    public RealmResource getRealmResource(String realm) {
        return keycloak.realm(realm);
    }

    public UserDTO getUserProfile(String realmName) {
        UsersResource users = keycloak.realm(realmName).users();
        String email = tokenHandler.getUsername();
        List<UserRepresentation> userRepresentations = users.searchByEmail(email, true);
        String userId = userRepresentations.get(0).getId();
        UserRepresentation userRepresentation = users.get(userId).toRepresentation();

        return UserDTO.builder()
                .id(UUID.fromString(userId))
                .email(userRepresentation.getEmail())
                .firstName(userRepresentation.getFirstName())
                .lastName(userRepresentation.getLastName())
                .build();
    }

    public UserDTO getUserProfileById(String realmName, String userId) {
        try {
            UsersResource users = keycloak.realm(realmName).users();
            UserRepresentation userRepresentation = users.get(userId).toRepresentation();

            return UserDTO.builder()
                    .id(UUID.fromString(userId))
                    .email(userRepresentation.getEmail())
                    .firstName(userRepresentation.getFirstName())
                    .lastName(userRepresentation.getLastName())
                    .build();
        } catch (Exception e) {
            throw new NotFoundException(ErrorMessage.USER_NOT_FOUND);
        }
    }

    public KeycloakBuilder getMasterKeycloakInstance() {
        return KeycloakBuilder.builder()
                .serverUrl(keycloakUrl)
                .realm(keycloakRealm)
                .clientId(masterKeycloakClientId)
                .grantType(OAuth2Constants.PASSWORD)
                .username(keycloakUsername)
                .password(keycloakPassword)
                .scope("openid");
    }

    public void updateUserProfile(String realmName, String userId, UserRepresentation userRepresentation) {
        UsersResource usersResource = keycloak.realm(realmName).users();
        usersResource.get(userId).update(userRepresentation);
    }

    public Map<String, UserDTO> getUserProfilesByIds(String realmName, List<String> senderIds) {
        UsersResource users = keycloak.realm(realmName).users();
        Map<String, UserDTO> userProfileMap = new HashMap<>();
        senderIds.forEach(senderId -> {
            try {
                UserRepresentation userRepresentation = users.get(senderId).toRepresentation();
                userProfileMap.put(senderId, UserDTO.builder()
                        .id(UUID.fromString(senderId))
                        .firstName(userRepresentation.getFirstName())
                        .lastName(userRepresentation.getLastName())
                        .build());
            } catch (Exception e) {
                userProfileMap.put(senderId, null);
            }
        });

        return userProfileMap;
    }

    public Map<String, RecommendDTO> getUserRecommendByIds(String realmName, List<String> userIds) {
        UsersResource users = keycloak.realm(realmName).users();

        String email = tokenHandler.getUsername();
        List<UserRepresentation> userRepresentations = users.searchByEmail(email, true);
        String currentUserId = userRepresentations.get(0).getId();

        Map<String, RecommendDTO> userProfileMap = new HashMap<>();
        userIds.forEach(userId -> {
            try {
                UserRepresentation userRepresentation = users.get(userId).toRepresentation();
                Optional<User> user = userRepository.findById(UUID.fromString(userId));
                UUID requestId = !user.get().getReceivedFriendRequests().isEmpty() ? user.get().getReceivedFriendRequests().get(0).getId() : null;
                if (requestId == null) {
                    requestId = !user.get().getSentFriendRequests().isEmpty() ? user.get().getSentFriendRequests().get(0).getId() : null;
                }
                boolean isFriend = checkIsFriend(user.get().getFriendships(), UUID.fromString(userId), UUID.fromString(currentUserId));
                UserRecommendDTO userRecommendDTO = UserRecommendDTO.builder()
                        .id(UUID.fromString(userId))
                        .firstName(userRepresentation.getFirstName())
                        .lastName(userRepresentation.getLastName())
                        .build();
                userProfileMap.put(userId, RecommendDTO.builder()
                        .user(userRecommendDTO)
                        .requestId(requestId)
                        .isFriend(isFriend)
                        .build());
            } catch (Exception e) {
                userProfileMap.put(userId, null);
            }
        });

        return userProfileMap;
    }

    private boolean checkIsFriend(List<Friendship> friendships, UUID friendId, UUID currentUserId) {
        return friendships.stream().anyMatch(friendship -> friendship.getUser().getId().equals(friendId) && friendship.getFriend().getId().equals(currentUserId));
    }
}
