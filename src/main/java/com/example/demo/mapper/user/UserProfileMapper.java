package com.example.demo.mapper.user;

import com.example.demo.dto.user.profile.UserProfileDTO;
import com.example.demo.dto.user.profile.UserProfileUpdateDTO;
import com.example.demo.model.user.UserProfile;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserProfileMapper {
    UserProfileMapper INSTANCE = Mappers.getMapper(UserProfileMapper.class);

    void mapUpdate(@MappingTarget UserProfile userProfile, UserProfileUpdateDTO profileDTO);

    UserProfileDTO toDto(UserProfile userProfile);
}
