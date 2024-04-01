package com.example.demo.service.friendrequest;

import com.example.demo.dto.ResponseDTO;
import com.example.demo.dto.friendrequest.FriendRequestDTO;
import com.example.demo.dto.friendrequest.SendRequestDTO;

import java.util.List;
import java.util.UUID;

public interface FriendRequestService {
    List<FriendRequestDTO> getFriendRequests();

    ResponseDTO sendFriendRequest(SendRequestDTO sendRequestDTO);

    ResponseDTO acceptFriendRequest(UUID requestId);

    ResponseDTO rejectFriendRequest(UUID requestId);
}
