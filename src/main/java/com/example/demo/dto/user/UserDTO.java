package com.example.demo.dto.user;

import com.example.demo.dto.user.avatar.UserAvatarDTO;
import com.example.demo.dto.user.cover.UserCoverDTO;
import com.example.demo.dto.user.experience.UserExperienceDTO;
import com.example.demo.dto.user.profile.UserProfileDTO;
import com.example.demo.dto.user.skill.UserSkillDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class UserDTO {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private UserProfileDTO profile;
    private UserAvatarDTO avatar;
    private UserCoverDTO cover;
    private List<UserSkillDTO> skills;
    private List<UserExperienceDTO> experiences;
}
