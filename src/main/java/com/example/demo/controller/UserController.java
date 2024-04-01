package com.example.demo.controller;

import com.example.demo.dto.ResponseDTO;
import com.example.demo.dto.user.UserDTO;
import com.example.demo.dto.user.UserUpdateDTO;
import com.example.demo.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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

    @PutMapping(value = "/{userId}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDTO> updateAvatar(@PathVariable("userId") String userId, @RequestPart("multipartFile") MultipartFile multipartFile) throws IOException {
        return ResponseEntity.ok(userService.updateAvatar(userId, multipartFile));
    }

    @PutMapping(value = "/{userId}/cover", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDTO> updateCover(@PathVariable("userId") String userId, @RequestPart("multipartFile") MultipartFile multipartFile) throws IOException {
        return ResponseEntity.ok(userService.updateCover(userId, multipartFile));
    }
}
