package com.example.demo.dto.user.skill;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserSkillCreateDTO {
    private String skillName;
    private String certificationName;
    private UUID userId;
}
