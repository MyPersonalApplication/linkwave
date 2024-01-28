package com.example.demo.service.friendrequest;

import com.example.demo.dto.ResponseDTO;

import java.util.UUID;

public interface FriendRequestService {
    ResponseDTO sendFriendRequest(UUID receiverId);
}
