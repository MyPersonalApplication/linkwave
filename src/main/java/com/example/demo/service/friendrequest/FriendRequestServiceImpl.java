package com.example.demo.service.friendrequest;

import com.example.demo.config.authentication.TokenHandler;
import com.example.demo.dto.ResponseDTO;
import com.example.demo.dto.friendrequest.FriendRequestDTO;
import com.example.demo.dto.friendrequest.SendRequestDTO;
import com.example.demo.dto.friendship.FriendShipDTO;
import com.example.demo.dto.user.UserDTO;
import com.example.demo.enums.ErrorMessage;
import com.example.demo.mapper.friend.FriendRequestMapper;
import com.example.demo.mapper.friend.FriendShipMapper;
import com.example.demo.model.friend.FriendRequest;
import com.example.demo.model.friend.Friendship;
import com.example.demo.model.user.User;
import com.example.demo.repository.FriendRequestRepository;
import com.example.demo.repository.FriendShipRepository;
import com.example.demo.service.keycloak.KeycloakService;
import com.example.demo.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FriendRequestServiceImpl implements FriendRequestService {
    private final String realmName = "linkwave";
    private final KeycloakService keycloakService;
    private final TokenHandler tokenHandler;
    private final UserService userService;
    private final FriendRequestRepository friendRequestRepository;
    private final FriendShipRepository friendShipRepository;

    @Override
    public List<FriendRequestDTO> getFriendRequests() {
        // Get user id from token
        UUID userId = tokenHandler.getUserId();

        // Load user by id
        User user = userService.loadUserById(userId);
        if (user == null) {
            throw new IllegalArgumentException(ErrorMessage.USER_NOT_FOUND);
        }

        // Get friend requests for the user
        List<FriendRequest> friendRequests = friendRequestRepository.findByReceiverId(userId);
        List<FriendRequestDTO> friendRequestDTOs = FriendRequestMapper.INSTANCE.toDtos(friendRequests);

        // Collect sender ids for batch retrieval
        List<String> senderIds = friendRequestDTOs.stream()
                .map(request -> String.valueOf(request.getSender().getId()))
                .collect(Collectors.toList());

        // Fetch user profiles for senders
        Map<String, UserDTO> userProfileMap = keycloakService.getUserProfilesByIds(realmName, senderIds);

        // Update sender details in friend request DTOs
        friendRequestDTOs.forEach(request -> {
            UserDTO userDTO = userProfileMap.get(String.valueOf(request.getSender().getId()));
            if (userDTO != null) {
                request.getSender().setFirstName(userDTO.getFirstName());
                request.getSender().setLastName(userDTO.getLastName());
            }
        });

        // Return friend requests
        return friendRequestDTOs;
    }

    @Override
    public ResponseDTO sendFriendRequest(SendRequestDTO sendRequestDTO) {
        // Get user id from token
        UUID userId = tokenHandler.getUserId();
        Optional<User> sender = Optional.ofNullable(userService.loadUserById(userId));
        if (sender.isEmpty()) {
            throw new IllegalArgumentException(ErrorMessage.USER_NOT_FOUND);
        }
        sendRequestDTO.setSenderId(userId);

        // Check if sender and receiver are the same
        if (userId.equals(sendRequestDTO.getReceiverId())) {
            throw new IllegalArgumentException(ErrorMessage.CANNOT_REQUEST_TO_YOURSELF);
        }

        // Check if receiver exists
        Optional<User> receiver = Optional.ofNullable(userService.loadUserById(sendRequestDTO.getReceiverId()));
        if (receiver.isEmpty()) {
            throw new IllegalArgumentException(ErrorMessage.USER_NOT_FOUND);
        }

        // Check if friend request already exists
        Optional<FriendRequest> existFriendRequestWasSend = Optional.ofNullable(friendRequestRepository.findBySenderIdAndReceiverId(userId, sendRequestDTO.getReceiverId()));
        Optional<FriendRequest> existFriendRequestWasReceived = Optional.ofNullable(friendRequestRepository.findBySenderIdAndReceiverId(sendRequestDTO.getReceiverId(), userId));
        if (existFriendRequestWasSend.isPresent() || existFriendRequestWasReceived.isPresent()) {
            throw new IllegalArgumentException(ErrorMessage.FRIEND_REQUEST_ALREADY_EXISTS);
        }

        // Send friend request
        FriendRequest friendRequest = FriendRequestMapper.INSTANCE.toEntity(sendRequestDTO);
        friendRequest.setCreatedAt(new Date());
        friendRequest.setUpdatedAt(new Date());
        friendRequestRepository.save(friendRequest);
        log.info(userId + " sent friend request to " + sendRequestDTO.getReceiverId());

        return ResponseDTO.builder()
                .message("Friend request was sent successfully")
                .build();
    }

    @Override
    public ResponseDTO acceptFriendRequest(UUID requestId) {
        // Get user id from token
        UUID userId = tokenHandler.getUserId();

        // Check if friend request exists
        Optional<FriendRequest> friendRequest = friendRequestRepository.findById(requestId);
        if (friendRequest.isEmpty()) {
            throw new IllegalArgumentException(ErrorMessage.FRIEND_REQUEST_NOT_FOUND);
        }

        // Check if friend request is for the user
        if (!friendRequest.get().getReceiver().getId().equals(userId)) {
            throw new IllegalArgumentException(ErrorMessage.FRIEND_REQUEST_NOT_FOUND);
        }

        // Accept friend request
        // Add friend to user's friend list
        FriendShipDTO addFriendToFriendListDTO = FriendShipDTO.builder()
                .userId(friendRequest.get().getReceiver().getId())
                .friendId(friendRequest.get().getSender().getId())
                .build();
        Friendship addFriendToFriendList = FriendShipMapper.INSTANCE.toEntity(addFriendToFriendListDTO);
        addFriendToFriendList.setCreatedAt(new Date());
        addFriendToFriendList.setUpdatedAt(new Date());
        friendShipRepository.save(addFriendToFriendList);

        // Add user to friend's friend list
        FriendShipDTO addUserToFriendListDTO = FriendShipDTO.builder()
                .userId(friendRequest.get().getSender().getId())
                .friendId(friendRequest.get().getReceiver().getId())
                .build();
        Friendship addUserToFriendList = FriendShipMapper.INSTANCE.toEntity(addUserToFriendListDTO);
        addUserToFriendList.setCreatedAt(new Date());
        addUserToFriendList.setUpdatedAt(new Date());
        friendShipRepository.save(addUserToFriendList);

        // Delete friend request
        friendRequestRepository.delete(friendRequest.get());

        return ResponseDTO.builder()
                .message("Friend request was accepted successfully")
                .build();
    }

    @Override
    public ResponseDTO rejectFriendRequest(UUID requestId) {
        // Get user id from token
        UUID userId = tokenHandler.getUserId();

        // Check if friend request exists
        Optional<FriendRequest> friendRequest = friendRequestRepository.findById(requestId);
        if (friendRequest.isEmpty()) {
            throw new IllegalArgumentException(ErrorMessage.FRIEND_REQUEST_NOT_FOUND);
        }

        // Check if friend request is for the user
        if (!friendRequest.get().getReceiver().getId().equals(userId)) {
            throw new IllegalArgumentException(ErrorMessage.FRIEND_REQUEST_NOT_FOUND);
        }

        // Delete friend request
        friendRequestRepository.delete(friendRequest.get());

        return ResponseDTO.builder()
                .message("Friend request was rejected successfully")
                .build();
    }
}
