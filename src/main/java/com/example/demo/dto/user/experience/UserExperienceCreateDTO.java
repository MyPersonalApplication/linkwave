package com.example.demo.dto.user.experience;

import com.example.demo.enums.ExperienceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserExperienceCreateDTO {
    private String companyOrSchoolName;
    private String positionOrDegree;
    private String description;
    private Date startDate;
    private Date endDate;
    private String location;
    private String experienceType;
}
