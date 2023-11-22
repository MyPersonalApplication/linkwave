package com.example.demo.config.authentication;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Setter
public class AccessTokenHandler implements TokenHandler {
    private String token;
    private String username;
    private UUID userId;

    @Override
    public String getToken() {
        return token;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public UUID getUserId() {
        return userId;
    }
}
