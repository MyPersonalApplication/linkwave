package com.example.demo.service.user;

import com.example.demo.controller.exception.NotFoundException;
import com.example.demo.dto.ResponseDTO;
import com.example.demo.dto.user.UserDTO;
import com.example.demo.dto.user.skill.UserSkillCreateDTO;
import com.example.demo.dto.user.skill.UserSkillDTO;
import com.example.demo.dto.user.skill.UserSkillUpdateDTO;
import com.example.demo.enums.ErrorMessage;
import com.example.demo.mapper.user.UserSkillMapper;
import com.example.demo.model.user.User;
import com.example.demo.model.user.UserSkill;
import com.example.demo.repository.user.UserRepository;
import com.example.demo.repository.user.UserSkillRepository;
import com.example.demo.service.keycloak.KeycloakService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserSkillService {
    private final KeycloakService keycloakService;
    private final String realmName = "linkwave";
    private final UserRepository userRepository;
    private final UserSkillRepository userSkillRepository;

    public UserSkillDTO createNewUserSkill(UserSkillCreateDTO userSkillCreateDTO) {
        UserDTO userDTO = keycloakService.getUserProfile(realmName);
        Optional<User> user = Optional.ofNullable(userRepository.findByEmail(userDTO.getEmail()));
        if (user.isEmpty()) {
            throw new NotFoundException(ErrorMessage.USER_NOT_FOUND);
        }
        UserSkill userSkill = UserSkill.builder()
                .skillName(userSkillCreateDTO.getSkillName())
                .certificationName(userSkillCreateDTO.getCertificationName())
                .user(user.get())
                .build();
        return UserSkillMapper.INSTANCE.toDto(userSkillRepository.save(userSkill));
    }

    public ResponseDTO updateUserSkill(String userSkillId, UserSkillUpdateDTO userSkillUpdateDTO) {
        UserSkill userSkill = userSkillRepository.findById(UUID.fromString(userSkillId)).orElseThrow(EntityNotFoundException::new);
        UserSkillMapper.INSTANCE.mapUpdate(userSkill, userSkillUpdateDTO);
        userSkillRepository.save(userSkill);
        return ResponseDTO.builder()
                .message("Update user skill successfully")
                .build();
    }

    public ResponseDTO deleteUserSkill(String id) {
        Optional<UserSkill> userSkill = userSkillRepository.findById(UUID.fromString(id));
        if (userSkill.isEmpty()) {
            throw new NotFoundException(ErrorMessage.USER_SKILL_NOT_FOUND);
        }
        userSkillRepository.deleteById(UUID.fromString(id));
        return ResponseDTO.builder()
                .message("Delete user skill successfully")
                .build();
    }
}
