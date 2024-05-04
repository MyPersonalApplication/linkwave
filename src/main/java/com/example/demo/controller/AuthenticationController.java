package com.example.demo.controller;

import com.example.demo.dto.ResponseDTO;
import com.example.demo.dto.authentication.AuthenticationResponse;
import com.example.demo.dto.authentication.ChangePasswordDTO;
import com.example.demo.dto.authentication.LoginDTO;
import com.example.demo.dto.authentication.RegisterDTO;
import com.example.demo.service.authentication.AuthenticationService;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginDTO authenticationRequest) {
        AuthenticationResponse response = authenticationService.authenticate(
                authenticationRequest.getEmail(), authenticationRequest.getPassword());
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/register")
    public ResponseEntity<ResponseDTO> register(@RequestBody RegisterDTO registerDTO) {
        ResponseDTO responseDTO = authenticationService.registerNewUser(registerDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping(value = "/reset-password")
    public void resetPassword(@RequestBody String email) {
        authenticationService.resetPassword(email);
    }

    @PostMapping(value = "/resend-verification-email")
    public void resendVerificationEmail(@RequestBody String email) {
        authenticationService.resendVerificationEmail(email);
    }
}
