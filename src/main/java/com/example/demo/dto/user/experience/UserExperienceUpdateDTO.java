package com.example.demo.dto.user.experience;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class UserExperienceUpdateDTO extends UserExperienceCreateDTO {
    private UUID id;
}
