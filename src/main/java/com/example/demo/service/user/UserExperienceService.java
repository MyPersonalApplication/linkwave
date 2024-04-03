package com.example.demo.service.user;

import com.example.demo.controller.exception.NotFoundException;
import com.example.demo.dto.ResponseDTO;
import com.example.demo.dto.user.UserDTO;
import com.example.demo.dto.user.experience.UserExperienceCreateDTO;
import com.example.demo.dto.user.experience.UserExperienceDTO;
import com.example.demo.dto.user.experience.UserExperienceUpdateDTO;
import com.example.demo.enums.ErrorMessage;
import com.example.demo.enums.ExperienceType;
import com.example.demo.mapper.user.UserExperienceMapper;
import com.example.demo.model.user.User;
import com.example.demo.model.user.UserExperience;
import com.example.demo.repository.user.UserExperienceRepository;
import com.example.demo.repository.user.UserRepository;
import com.example.demo.service.keycloak.KeycloakService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserExperienceService {
    private final KeycloakService keycloakService;
    private final String realmName = "linkwave";
    private final UserRepository userRepository;
    private final UserExperienceRepository userExperienceRepository;

    public UserExperienceDTO getExperience(String id) {
        Optional<UserExperience> userExperience = userExperienceRepository.findById(UUID.fromString(id));
        if (userExperience.isEmpty()) {
            throw new NotFoundException(ErrorMessage.USER_EXPERIENCE_NOT_FOUND);
        }

        return UserExperienceMapper.INSTANCE.toDto(userExperience.get());
    }

    public UserExperienceDTO createNewUserExperience(UserExperienceCreateDTO userExperienceCreateDTO) {
        UserDTO userDTO = keycloakService.getUserProfile(realmName);
        Optional<User> user = Optional.ofNullable(userRepository.findByEmail(userDTO.getEmail()));
        if (user.isEmpty()) {
            throw new NotFoundException(ErrorMessage.USER_NOT_FOUND);
        }
        if (!isValidExperienceType(String.valueOf(userExperienceCreateDTO.getExperienceType()))) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_EXPERIENCE_TYPE);
        }
        UserExperience userExperience = UserExperience.builder()
                .companyOrSchoolName(userExperienceCreateDTO.getCompanyOrSchoolName())
                .positionOrDegree(userExperienceCreateDTO.getPositionOrDegree())
                .description(userExperienceCreateDTO.getDescription())
                .startDate(userExperienceCreateDTO.getStartDate())
                .endDate(userExperienceCreateDTO.getEndDate())
                .location(userExperienceCreateDTO.getLocation())
                .experienceType(ExperienceType.valueOf(userExperienceCreateDTO.getExperienceType()))
                .user(user.get())
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();
        return UserExperienceMapper.INSTANCE.toDto(userExperienceRepository.save(userExperience));
    }

    private static boolean isValidExperienceType(String input) {
        for (ExperienceType type : ExperienceType.values()) {
            if (type.name().equalsIgnoreCase(input)) {
                return true;
            }
        }
        return false;
    }

    public ResponseDTO updateUserExperience(String id, UserExperienceUpdateDTO userExperienceUpdateDTO) {
        Optional<UserExperience> userExperience = userExperienceRepository.findById(UUID.fromString(id));
        if (userExperience.isEmpty()) {
            throw new NotFoundException(ErrorMessage.USER_EXPERIENCE_NOT_FOUND);
        }
        UserExperienceMapper.INSTANCE.mapUpdate(userExperience.get(), userExperienceUpdateDTO);
        userExperience.get().setUpdatedAt(new Date());
        userExperienceRepository.save(userExperience.get());
        return ResponseDTO.builder()
                .message("Update user experience successfully")
                .build();
    }

    public ResponseDTO deleteUserExperience(String id) {
        Optional<UserExperience> userExperience = userExperienceRepository.findById(UUID.fromString(id));
        if (userExperience.isEmpty()) {
            throw new NotFoundException(ErrorMessage.USER_EXPERIENCE_NOT_FOUND);
        }
        userExperienceRepository.deleteById(UUID.fromString(id));
        return ResponseDTO.builder()
                .message("Delete user experience successfully")
                .build();
    }
}
