package com.example.demo.mapper.user;

import com.example.demo.dto.user.avatar.UserAvatarUpdateDTO;
import com.example.demo.dto.user.cover.UserCoverDTO;
import com.example.demo.dto.user.cover.UserCoverUpdateDTO;
import com.example.demo.model.user.UserAvatar;
import com.example.demo.model.user.UserCover;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserCoverMapper {
    UserCoverMapper INSTANCE = Mappers.getMapper(UserCoverMapper.class);

    void mapUpdate(@MappingTarget UserCover userCover, UserCoverUpdateDTO userCoverUpdateDTO);

    UserCoverDTO toDto(UserCover userCover);
}
