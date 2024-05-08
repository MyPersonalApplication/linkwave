package com.example.demo.dto.user.experience;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.UUID;

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
    private UUID userId;
}
