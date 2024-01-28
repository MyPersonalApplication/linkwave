package com.example.demo.dto.user.experience;

import com.example.demo.enums.ExperienceType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class UserExperienceDTO {
    private UUID id;
    private String companyOrSchoolName;
    private String positionOrDegree;
    private String description;
    private Date startDate;
    private Date endDate;
    private String location;
    private ExperienceType experienceType;
}
