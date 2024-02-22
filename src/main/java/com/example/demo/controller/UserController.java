package com.example.demo.controller;

import com.example.demo.dto.ResponseDTO;
import com.example.demo.dto.user.UserDTO;
import com.example.demo.dto.user.UserUpdateDTO;
import com.example.demo.dto.user.profile.UserProfileUpdateDTO;
import com.example.demo.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/profile")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping(value = "/me")
    public ResponseEntity<UserDTO> getCurrentProfile() {
        return ResponseEntity.ok(userService.getCurrentProfile());
    }

    @GetMapping(value = "/{userId}")
    public ResponseEntity<UserDTO> getProfile(@PathVariable String userId) {
        return ResponseEntity.ok(userService.getProfileByUserId(userId));
    }

    @PutMapping(value = "/{userId}")
    public ResponseEntity<ResponseDTO> updateProfile(@PathVariable String userId, @RequestBody UserUpdateDTO userUpdateDTO) {
        return ResponseEntity.ok(userService.updateProfile(userId, userUpdateDTO));
    }
}
