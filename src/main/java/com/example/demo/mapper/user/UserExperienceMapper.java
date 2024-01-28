package com.example.demo.mapper.user;

import com.example.demo.dto.user.experience.UserExperienceDTO;
import com.example.demo.dto.user.experience.UserExperienceUpdateDTO;
import com.example.demo.model.user.UserExperience;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserExperienceMapper {
    UserExperienceMapper INSTANCE = Mappers.getMapper(UserExperienceMapper.class);

    void mapUpdate(@MappingTarget UserExperience userExperience, UserExperienceUpdateDTO userExperienceUpdateDTO);

    UserExperienceDTO toDto(UserExperience userExperience);
}
