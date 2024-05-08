package com.example.demo.mapper.user;

import com.example.demo.dto.user.experience.UserExperienceCreateDTO;
import com.example.demo.dto.user.experience.UserExperienceDTO;
import com.example.demo.dto.user.experience.UserExperienceUpdateDTO;
import com.example.demo.model.user.UserExperience;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserExperienceMapper {
    UserExperienceMapper INSTANCE = Mappers.getMapper(UserExperienceMapper.class);

    void mapUpdate(@MappingTarget UserExperience userExperience, UserExperienceUpdateDTO userExperienceUpdateDTO);

    UserExperienceDTO toDto(UserExperience userExperience);

    @Mapping(target = "user.id", source = "userId")
    UserExperience toEntity(UserExperienceCreateDTO userExperienceCreateDTO);
}
