package com.example.demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/check-health")
@RequiredArgsConstructor
public class CheckController {
    @GetMapping
    public ResponseEntity<String> checkHealth() {
        return ResponseEntity.ok().body("Health check");
    }
}
