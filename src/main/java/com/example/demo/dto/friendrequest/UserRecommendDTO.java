package com.example.demo.dto.friendrequest;

import com.example.demo.dto.user.avatar.UserAvatarDTO;
import com.example.demo.dto.user.profile.UserProfileDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class UserRecommendDTO {
    private UUID id;
    private String firstName;
    private String lastName;
    private UserAvatarDTO avatar;
    private UserProfileDTO profile;
}
