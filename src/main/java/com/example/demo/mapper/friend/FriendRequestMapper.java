package com.example.demo.mapper.friend;

import com.example.demo.dto.friendrequest.FriendRequestDTO;
import com.example.demo.dto.friendrequest.SendRequestDTO;
import com.example.demo.model.friend.FriendRequest;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface FriendRequestMapper {
    FriendRequestMapper INSTANCE = Mappers.getMapper(FriendRequestMapper.class);

    @Mapping(target = "sender.avatar", source = "sender.userAvatar")
    @Mapping(target = "receiver.avatar", source = "receiver.userAvatar")
    FriendRequestDTO toDto(FriendRequest friendRequest);

    List<FriendRequestDTO> toDtos(List<FriendRequest> friendRequestList);

    @Mapping(target = "sender.id", source = "senderId")
    @Mapping(target = "receiver.id", source = "receiverId")
    FriendRequest toEntity(SendRequestDTO sendRequestDTO);
}
