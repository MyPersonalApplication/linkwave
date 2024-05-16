package com.example.demo.controller;

import com.example.demo.dto.ResponseDTO;
import com.example.demo.dto.authentication.ChangePasswordDTO;
import com.example.demo.dto.base.SearchResultDTO;
import com.example.demo.dto.user.UserDTO;
import com.example.demo.dto.user.UserUpdateDTO;
import com.example.demo.dto.user.experience.UserExperienceCreateDTO;
import com.example.demo.dto.user.experience.UserExperienceDTO;
import com.example.demo.dto.user.experience.UserExperienceUpdateDTO;
import com.example.demo.dto.user.skill.UserSkillCreateDTO;
import com.example.demo.dto.user.skill.UserSkillDTO;
import com.example.demo.dto.user.skill.UserSkillUpdateDTO;
import com.example.demo.service.user.UserExperienceService;
import com.example.demo.service.user.UserService;
import com.example.demo.service.user.UserSkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/users")
@PreAuthorize("hasRole('USER')")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserExperienceService userExperienceService;
    private final UserSkillService userSkillService;

    @GetMapping()
    public ResponseEntity<SearchResultDTO> getCurrentUser(@RequestParam(required = false, defaultValue = "0") int page,
                                                          @RequestParam(required = false, defaultValue = "10") int pageSize) {
        return ResponseEntity.ok(userService.getAll(page, pageSize));
    }

    @PostMapping(value = "/change-password")
    public ResponseEntity<ResponseDTO> changePassword(@RequestBody ChangePasswordDTO changePasswordDTO) {
        ResponseDTO responseDTO = userService.changePassword(changePasswordDTO.getOldPassword(), changePasswordDTO.getNewPassword());
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping(value = "/profile/me")
    public ResponseEntity<UserDTO> getCurrentProfile() {
        return ResponseEntity.ok(userService.getCurrentProfile());
    }

    @GetMapping(value = "/profile/{userId}")
    public ResponseEntity<UserDTO> getProfile(@PathVariable String userId) {
        return ResponseEntity.ok(userService.getProfileByUserId(userId));
    }

    @PutMapping(value = "/profile/{userId}")
    public ResponseEntity<UserDTO> updateProfile(@PathVariable String userId, @RequestBody UserUpdateDTO userUpdateDTO) {
        return ResponseEntity.ok(userService.updateProfile(userId, userUpdateDTO));
    }

    @PutMapping(value = "/avatar/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDTO> updateAvatar(@PathVariable("userId") String userId, @RequestPart("multipartFile") MultipartFile multipartFile) throws IOException {
        return ResponseEntity.ok(userService.updateAvatar(userId, multipartFile));
    }

    @PutMapping(value = "/cover/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDTO> updateCover(@PathVariable("userId") String userId, @RequestPart("multipartFile") MultipartFile multipartFile) throws IOException {
        return ResponseEntity.ok(userService.updateCover(userId, multipartFile));
    }

    @PostMapping("/experience")
    public ResponseEntity<UserExperienceDTO> createExperience(@RequestBody UserExperienceCreateDTO userExperienceCreateDTO) {
        return ResponseEntity.ok(userExperienceService.createNewUserExperience(userExperienceCreateDTO));
    }

    @PutMapping("/experience/{id}")
    public ResponseEntity<ResponseDTO> updateExperience(@PathVariable String id, @RequestBody UserExperienceUpdateDTO userExperienceUpdateDTO) {
        return ResponseEntity.ok(userExperienceService.updateUserExperience(id, userExperienceUpdateDTO));
    }

    @DeleteMapping("/experience/{id}")
    public ResponseEntity<ResponseDTO> deleteExperience(@PathVariable String id) {
        return ResponseEntity.ok(userExperienceService.deleteUserExperience(id));
    }

    @PostMapping("/skill")
    public ResponseEntity<UserSkillDTO> createSkill(@RequestBody UserSkillCreateDTO userSkillCreateDTO) {
        return ResponseEntity.ok(userSkillService.createNewUserSkill(userSkillCreateDTO));
    }

    @PutMapping("/skill/{id}")
    public ResponseEntity<ResponseDTO> updateSkill(@PathVariable String id, @RequestBody UserSkillUpdateDTO userSkillUpdateDTO) {
        return ResponseEntity.ok(userSkillService.updateUserSkill(id, userSkillUpdateDTO));
    }

    @DeleteMapping("/skill/{id}")
    public ResponseEntity<ResponseDTO> deleteSkill(@PathVariable String id) {
        return ResponseEntity.ok(userSkillService.deleteUserSkill(id));
    }
}
