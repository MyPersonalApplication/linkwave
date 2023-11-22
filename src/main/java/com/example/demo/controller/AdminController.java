package com.example.demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {
    @GetMapping
    public ResponseEntity<String> demo() {
        return ResponseEntity.ok().body("Get all users in admin");
    }
}
