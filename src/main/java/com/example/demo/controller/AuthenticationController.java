package com.example.demo.controller;

import com.example.demo.dto.authentication.AuthenticationResponse;
import com.example.demo.dto.authentication.LoginDTO;
import com.example.demo.dto.authentication.RegisterDTO;
import com.example.demo.service.authentication.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping(value = "/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginDTO authenticationRequest) throws AccessDeniedException {
        AuthenticationResponse response = authenticationService.authenticate(
                authenticationRequest.getEmail(), authenticationRequest.getPassword());
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/register")
    public ResponseEntity<String> register(@RequestBody RegisterDTO registerDTO) {
        return ResponseEntity.ok(authenticationService.registerNewUser(registerDTO));
    }
}
