package com.example.demo.dto.user.profile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class UserProfileDTO {
    private boolean gender;
    private Date dateOfBirth;
    private String country;
    private String address;
    private String aboutMe;
    private String phoneNumber;
    private List<String> hobbies;
    private String avatarUrl;
    private String coverUrl;
}
