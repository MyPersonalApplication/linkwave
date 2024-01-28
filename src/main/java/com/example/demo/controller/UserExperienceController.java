package com.example.demo.controller;

import com.example.demo.dto.ResponseDTO;
import com.example.demo.dto.user.experience.UserExperienceCreateDTO;
import com.example.demo.dto.user.experience.UserExperienceDTO;
import com.example.demo.dto.user.experience.UserExperienceUpdateDTO;
import com.example.demo.service.user.UserExperienceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/experience")
@RequiredArgsConstructor
public class UserExperienceController {
    private final UserExperienceService userExperienceService;

    @PostMapping
    public ResponseEntity<UserExperienceDTO> create(@RequestBody UserExperienceCreateDTO userExperienceCreateDTO) {
        return ResponseEntity.ok(userExperienceService.createNewUserExperience(userExperienceCreateDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO> update(@PathVariable String id, @RequestBody UserExperienceUpdateDTO userExperienceUpdateDTO) {
        return ResponseEntity.ok(userExperienceService.updateUserExperience(id, userExperienceUpdateDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO> delete(@PathVariable String id) {
        return ResponseEntity.ok(userExperienceService.deleteUserExperience(id));
    }
}
