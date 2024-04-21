package com.example.demo.mapper.user;

import com.example.demo.dto.user.UserDTO;
import com.example.demo.model.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "profile", source = "userProfile")
    @Mapping(target = "avatar", source = "userAvatar")
    @Mapping(target = "cover", source = "userCover")
    @Mapping(target = "skills", source = "userSkills")
    @Mapping(target = "experiences", source = "userExperiences")
    UserDTO toDto(User user);
}
