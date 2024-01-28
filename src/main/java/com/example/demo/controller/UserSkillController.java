package com.example.demo.controller;

import com.example.demo.dto.ResponseDTO;
import com.example.demo.dto.user.skill.UserSkillCreateDTO;
import com.example.demo.dto.user.skill.UserSkillDTO;
import com.example.demo.dto.user.skill.UserSkillUpdateDTO;
import com.example.demo.service.user.UserSkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/skill")
@RequiredArgsConstructor
public class UserSkillController {
    private final UserSkillService userSkillService;

    @PostMapping
    public ResponseEntity<UserSkillDTO> create(@RequestBody UserSkillCreateDTO userSkillCreateDTO) {
        return ResponseEntity.ok(userSkillService.createNewUserSkill(userSkillCreateDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO> update(@PathVariable String id, @RequestBody UserSkillUpdateDTO userSkillUpdateDTO) {
        return ResponseEntity.ok(userSkillService.updateUserSkill(id, userSkillUpdateDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO> delete(@PathVariable String id) {
        return ResponseEntity.ok(userSkillService.deleteUserSkill(id));
    }
}
