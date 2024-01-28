package com.example.demo.service.keycloak;

import com.example.demo.config.authentication.TokenHandler;
import com.example.demo.dto.user.UserDTO;
import com.example.demo.enums.ErrorMessage;
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
        UsersResource users = keycloak.realm(realmName).users();
        UserRepresentation userRepresentation = users.get(userId).toRepresentation();

        return UserDTO.builder()
                .id(UUID.fromString(userId))
                .email(userRepresentation.getEmail())
                .firstName(userRepresentation.getFirstName())
                .lastName(userRepresentation.getLastName())
                .build();
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
}
