package com.example.demo.dto.user;

import com.example.demo.dto.user.profile.UserProfileDTO;
import com.example.demo.dto.user.profile.UserProfileUpdateDTO;
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
public class UserUpdateDTO {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private UserProfileUpdateDTO profile;
}
