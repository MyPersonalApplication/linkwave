package com.example.demo.config.authentication;

import java.util.UUID;

public interface TokenHandler {
    String getToken();
    String getUsername();
    UUID getUserId();
}
