package com.example.demo.config;

import lombok.Getter;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

@Getter
@ConfigurationProperties(prefix = "keycloak")
public class KeycloakConfig {
    @Value("${keycloak.url}")
    private String keycloakUrl;
    @Value("${keycloak.realm}")
    private String keycloakRealm;
    @Value("${keycloak.master-client-id}")
    private String masterKeycloakClientId;
    @Value("${keycloak.username}")
    private String keycloakUsername;
    @Value("${keycloak.password}")
    private String keycloakPassword;

    @Bean
    public Keycloak keycloak() {
        return KeycloakBuilder.builder()
                .serverUrl(keycloakUrl)
                .realm(keycloakRealm)
                .clientId(masterKeycloakClientId)
                .grantType(OAuth2Constants.PASSWORD)
                .username(keycloakUsername)
                .password(keycloakPassword)
                .scope("openid")
                .build();
    }
}
