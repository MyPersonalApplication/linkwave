package com.example.demo.service.friendrequest;

import com.example.demo.dto.ResponseDTO;
import com.example.demo.dto.base.SearchResultDTO;
import com.example.demo.dto.friendrequest.FriendRequestDTO;
import com.example.demo.dto.friendrequest.RecommendDTO;
import com.example.demo.dto.friendrequest.SendRequestDTO;
import com.example.demo.dto.user.UserDTO;

import java.util.List;
import java.util.UUID;

public interface FriendRequestService {
    List<FriendRequestDTO> getFriendRequests(int limit);

    FriendRequestDTO getFriendRequestById(UUID requestId);

    List<RecommendDTO> getRecommendedFriends(int limit);

    ResponseDTO sendFriendRequest(SendRequestDTO sendRequestDTO);

    ResponseDTO acceptFriendRequest(UUID requestId);

    ResponseDTO rejectFriendRequest(UUID requestId);

    ResponseDTO deleteFriendRequest(UUID requestId);

    SearchResultDTO searchFriend(String query, int page, int pageSize);
}
