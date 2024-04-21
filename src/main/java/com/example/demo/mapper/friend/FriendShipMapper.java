package com.example.demo.mapper.friend;

import com.example.demo.dto.friendship.FriendShipCreateDTO;
import com.example.demo.dto.friendship.FriendShipDTO;
import com.example.demo.model.friend.Friendship;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface FriendShipMapper {
    FriendShipMapper INSTANCE = Mappers.getMapper(FriendShipMapper.class);

    @Mapping(target = "user.id", source = "userId")
    @Mapping(target = "friend.id", source = "friendId")
    Friendship toEntity(FriendShipCreateDTO friendShipCreateDTO);

    @Mapping(target = "user.avatar", source = "friend.userAvatar")
    FriendShipDTO toDto(Friendship friendship);
}
