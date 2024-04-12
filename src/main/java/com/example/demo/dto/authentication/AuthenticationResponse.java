package com.example.demo.dto.authentication;

import com.example.demo.dto.user.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.keycloak.representations.AccessTokenResponse;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    private AccessTokenResponse accessTokenResponse;
    private List<String> roles;
    private UserDTO user;
}
