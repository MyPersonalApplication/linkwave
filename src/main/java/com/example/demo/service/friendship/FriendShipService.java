package com.example.demo.service.friendship;

import com.example.demo.dto.ResponseDTO;
import com.example.demo.dto.friendship.FriendShipDTO;

import java.util.List;
import java.util.UUID;

public interface FriendShipService {
    List<FriendShipDTO> getFriendList();
    ResponseDTO unfriend(UUID friendId);
}
