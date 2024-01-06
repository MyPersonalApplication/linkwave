package com.example.demo.controller;

import com.example.demo.dto.user.UserDTO;
import com.example.demo.service.authentication.AuthenticationService;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER') or hasRole('SUPER_ADMIN') or hasRole('ADMIN')")
public class UserController {
    private final AuthenticationService authenticationService;

    @GetMapping(value = "/profile")
    public ResponseEntity<UserDTO> getProfile() {
        return ResponseEntity.ok(authenticationService.getProfile());
    }
}
