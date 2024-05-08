package com.example.demo.mapper.user;

import com.example.demo.dto.user.skill.UserSkillCreateDTO;
import com.example.demo.dto.user.skill.UserSkillDTO;
import com.example.demo.dto.user.skill.UserSkillUpdateDTO;
import com.example.demo.model.user.UserSkill;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserSkillMapper {
    UserSkillMapper INSTANCE = Mappers.getMapper(UserSkillMapper.class);

    void mapUpdate(@MappingTarget UserSkill userSkill, UserSkillUpdateDTO userSkillUpdateDTO);

    UserSkillDTO toDto(UserSkill userSkill);

    @Mapping(target = "user.id", source = "userId")
    UserSkill toEntity(UserSkillCreateDTO userSkillCreateDTO);
}
