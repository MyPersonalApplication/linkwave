package com.example.demo.service.user;

import com.example.demo.controller.exception.NotFoundException;
import com.example.demo.dto.ResponseDTO;
import com.example.demo.dto.user.UserDTO;
import com.example.demo.dto.user.UserUpdateDTO;
import com.example.demo.dto.user.experience.UserExperienceDTO;
import com.example.demo.dto.user.profile.UserProfileDTO;
import com.example.demo.dto.user.skill.UserSkillDTO;
import com.example.demo.enums.ErrorMessage;
import com.example.demo.mapper.user.UserExperienceMapper;
import com.example.demo.mapper.user.UserProfileMapper;
import com.example.demo.mapper.user.UserSkillMapper;
import com.example.demo.model.user.User;
import com.example.demo.model.user.UserExperience;
import com.example.demo.model.user.UserProfile;
import com.example.demo.model.user.UserSkill;
import com.example.demo.repository.user.UserExperienceRepository;
import com.example.demo.repository.user.UserProfileRepository;
import com.example.demo.repository.user.UserRepository;
import com.example.demo.repository.user.UserSkillRepository;
import com.example.demo.service.keycloak.KeycloakService;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final KeycloakService keycloakService;
    private final String realmName = "linkwave";
    private final UserProfileRepository userProfileRepository;
    private final UserSkillRepository userSkillRepository;
    private final UserExperienceRepository userExperienceRepository;
    private final UserRepository userRepository;

    public UserDTO getCurrentProfile() {
        // Get user profile from keycloak
        UserDTO userDTO = keycloakService.getUserProfile(realmName);
        return buildUserDTO(userDTO, userDTO.getId());
    }

    public UserDTO getProfileByUserId(String userId) {
        // Get user profile from keycloak
        UserDTO userDTO = keycloakService.getUserProfileById(realmName, userId);
        return buildUserDTO(userDTO, userDTO.getId());
    }

    private UserDTO buildUserDTO(UserDTO userDTO, UUID userId) {
        Optional<UserProfile> userProfile = Optional.ofNullable(userProfileRepository.findByUserId(userId));
        if (userProfile.isEmpty()) {
            throw new NotFoundException(ErrorMessage.USER_PROFILE_NOT_FOUND);
        }
        UserProfileDTO userProfileDTO = UserProfileMapper.INSTANCE.toDto(userProfile.get());

        Optional<List<UserSkill>> userSkill = Optional.ofNullable(userSkillRepository.findByUserId(userId));
        if (userSkill.isEmpty()) {
            throw new NotFoundException(ErrorMessage.USER_SKILL_NOT_FOUND);
        }
        List<UserSkillDTO> userSkillDTO = userSkill.get().stream().map(UserSkillMapper.INSTANCE::toDto).toList();

        Optional<List<UserExperience>> userExperience = Optional.ofNullable(userExperienceRepository.findByUserId(userId));
        if (userExperience.isEmpty()) {
            throw new NotFoundException(ErrorMessage.USER_EXPERIENCE_NOT_FOUND);
        }
        List<UserExperienceDTO> userExperienceDTO = userExperience.get().stream().map(UserExperienceMapper.INSTANCE::toDto).toList();

        userDTO.setProfile(userProfileDTO);
        userDTO.setSkills(userSkillDTO);
        userDTO.setExperiences(userExperienceDTO);

        return userDTO;
    }

    public User loadUserByUsername(String username) {
        Optional<User> user = Optional.ofNullable(userRepository.findByEmail(username));
        if (user.isEmpty()) {
            throw new NotFoundException(ErrorMessage.USER_NOT_FOUND);
        }
        return user.get();
    }

    public User loadUserById(UUID userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new NotFoundException(ErrorMessage.USER_NOT_FOUND);
        }
        return user.get();
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public ResponseDTO updateProfile(String userId, UserUpdateDTO userUpdateDTO) {
        Optional<UserProfile> userProfile = Optional.ofNullable(userProfileRepository.findByUserId(UUID.fromString(userId)));
        if (userProfile.isEmpty()) {
            throw new NotFoundException(ErrorMessage.USER_PROFILE_NOT_FOUND);
        }

        // Update user profile in keycloak
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setFirstName(userUpdateDTO.getFirstName());
        userRepresentation.setLastName(userUpdateDTO.getLastName());
        userRepresentation.setEmail(userUpdateDTO.getEmail());
        keycloakService.updateUserProfile(realmName, userId, userRepresentation);

        // Update user profile in database
        if (userUpdateDTO.getProfile().getId() == null) {
            userUpdateDTO.getProfile().setId(UUID.fromString(userId));
        }
        UserProfileMapper.INSTANCE.mapUpdate(userProfile.get(), userUpdateDTO.getProfile());
        userProfileRepository.save(userProfile.get());

        return ResponseDTO.builder()
                .message("Update profile successfully")
                .build();
    }
}
