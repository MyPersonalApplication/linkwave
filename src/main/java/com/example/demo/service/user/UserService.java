package com.example.demo.service.user;

import com.example.demo.controller.exception.InvalidDataException;
import com.example.demo.controller.exception.NotFoundException;
import com.example.demo.dto.ResponseDTO;
import com.example.demo.dto.user.UserDTO;
import com.example.demo.dto.user.UserUpdateDTO;
import com.example.demo.dto.user.avatar.UserAvatarDTO;
import com.example.demo.dto.user.avatar.UserAvatarUpdateDTO;
import com.example.demo.dto.user.cover.UserCoverDTO;
import com.example.demo.dto.user.cover.UserCoverUpdateDTO;
import com.example.demo.dto.user.experience.UserExperienceDTO;
import com.example.demo.dto.user.profile.UserProfileDTO;
import com.example.demo.dto.user.skill.UserSkillDTO;
import com.example.demo.enums.ErrorMessage;
import com.example.demo.mapper.user.*;
import com.example.demo.model.user.*;
import com.example.demo.repository.user.*;
import com.example.demo.service.cloudinary.CloudinaryService;
import com.example.demo.service.keycloak.KeycloakService;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {
    private final KeycloakService keycloakService;
    private final String realmName = "linkwave";
    private final UserProfileRepository userProfileRepository;
    private final UserAvatarRepository userAvatarRepository;
    private final UserCoverRepository userCoverRepository;
    private final UserSkillRepository userSkillRepository;
    private final UserExperienceRepository userExperienceRepository;
    private final UserRepository userRepository;
    private final CloudinaryService cloudinaryService;

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
        // Get user profile from database
        Optional<UserProfile> userProfile = Optional.ofNullable(userProfileRepository.findByUserId(userId));
        if (userProfile.isEmpty()) {
            throw new NotFoundException(ErrorMessage.USER_PROFILE_NOT_FOUND);
        }
        UserProfileDTO userProfileDTO = UserProfileMapper.INSTANCE.toDto(userProfile.get());

        // Get user avatar and cover from database
        Optional<UserAvatar> userAvatar = Optional.ofNullable(userAvatarRepository.findByUserId(userId));
        if (userAvatar.isEmpty()) {
            throw new NotFoundException(ErrorMessage.USER_AVATAR_NOT_FOUND);
        }
        UserAvatarDTO userAvatarDTO = UserAvatarMapper.INSTANCE.toDto(userAvatar.get());

        Optional<UserCover> userCover = Optional.ofNullable(userCoverRepository.findByUserId(userId));
        if (userCover.isEmpty()) {
            throw new NotFoundException(ErrorMessage.USER_COVER_NOT_FOUND);
        }
        UserCoverDTO userCoverDTO = UserCoverMapper.INSTANCE.toDto(userCover.get());

        // Get user skill and experience from database
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
        userDTO.setAvatar(userAvatarDTO);
        userDTO.setCover(userCoverDTO);
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

    public ResponseDTO updateAvatar(String userId, MultipartFile multipartFile) throws IOException {
        Optional<UserAvatar> userAvatar = Optional.ofNullable(userAvatarRepository.findByUserId(UUID.fromString(userId)));
        if (userAvatar.isEmpty()) {
            throw new NotFoundException(ErrorMessage.USER_AVATAR_NOT_FOUND);
        }

        if (userAvatar.get().getImageId() != null && userAvatar.get().getImageUrl() != null) {
            // Delete old avatar in cloudinary
            cloudinaryService.deleteImage(userAvatar.get().getImageId());
        }

        // Check if the file is an image
        BufferedImage bi = ImageIO.read(multipartFile.getInputStream());
        if (bi == null) {
            throw new InvalidDataException("Invalid image");
        }
        Map result = cloudinaryService.uploadImage(multipartFile);

        // Update user avatar in cloudinary
        UserAvatarUpdateDTO userAvatarUpdateDTO = UserAvatarUpdateDTO.builder()
                .imageUrl((String) result.get("url"))
                .imageId((String) result.get("public_id"))
                .build();

        // Update user avatar in database
        if (userAvatarUpdateDTO.getId() == null) {
            userAvatarUpdateDTO.setId(UUID.fromString(userId));
        }
        UserAvatarMapper.INSTANCE.mapUpdate(userAvatar.get(), userAvatarUpdateDTO);
        userAvatar.get().setUpdatedAt(new Date());
        userAvatarRepository.save(userAvatar.get());

        return ResponseDTO.builder()
                .message("Update avatar successfully")
                .build();
    }

    public ResponseDTO updateCover(String userId, MultipartFile multipartFile) throws IOException {
        Optional<UserCover> userCover = Optional.ofNullable(userCoverRepository.findByUserId(UUID.fromString(userId)));
        if (userCover.isEmpty()) {
            throw new NotFoundException(ErrorMessage.USER_COVER_NOT_FOUND);
        }

        if (userCover.get().getImageId() != null && userCover.get().getImageUrl() != null) {
            // Delete old avatar in cloudinary
            cloudinaryService.deleteImage(userCover.get().getImageId());
        }

        // Check if the file is an image
        BufferedImage bi = ImageIO.read(multipartFile.getInputStream());
        if (bi == null) {
            throw new InvalidDataException("Invalid image");
        }
        Map result = cloudinaryService.uploadImage(multipartFile);

        // Update user avatar in cloudinary
        UserCoverUpdateDTO userCoverUpdateDTO = UserCoverUpdateDTO.builder()
                .imageUrl((String) result.get("url"))
                .imageId((String) result.get("public_id"))
                .build();

        // Update user avatar in database
        if (userCoverUpdateDTO.getId() == null) {
            userCoverUpdateDTO.setId(UUID.fromString(userId));
        }
        UserCoverMapper.INSTANCE.mapUpdate(userCover.get(), userCoverUpdateDTO);
        userCover.get().setUpdatedAt(new Date());
        userCoverRepository.save(userCover.get());

        return ResponseDTO.builder()
                .message("Update cover successfully")
                .build();
    }
}
