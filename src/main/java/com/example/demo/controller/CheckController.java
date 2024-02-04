package com.example.demo.controller;

import com.example.demo.dto.ResponseDTO;
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
    public ResponseEntity<ResponseDTO> checkHealth() {
        ResponseDTO responseDTO = ResponseDTO.builder()
                .message("Health check")
                .build();
        return ResponseEntity.ok().body(responseDTO);
    }
}
