package com.example.demo.mapper.user;

import com.example.demo.dto.user.avatar.UserAvatarDTO;
import com.example.demo.dto.user.avatar.UserAvatarUpdateDTO;
import com.example.demo.model.user.UserAvatar;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserAvatarMapper {
    UserAvatarMapper INSTANCE = Mappers.getMapper(UserAvatarMapper.class);

    void mapUpdate(@MappingTarget UserAvatar userAvatar, UserAvatarUpdateDTO userAvatarUpdateDTO);

    UserAvatarDTO toDto(UserAvatar userAvatar);
}
