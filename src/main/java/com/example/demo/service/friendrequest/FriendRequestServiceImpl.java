package com.example.demo.service.friendrequest;

import com.example.demo.config.authentication.TokenHandler;
import com.example.demo.controller.exception.NotFoundException;
import com.example.demo.dto.ResponseDTO;
import com.example.demo.dto.friendrequest.FriendRequestDTO;
import com.example.demo.dto.friendrequest.RecommendDTO;
import com.example.demo.dto.friendrequest.SendRequestDTO;
import com.example.demo.dto.friendrequest.UserRecommendDTO;
import com.example.demo.dto.friendship.FriendShipCreateDTO;
import com.example.demo.dto.user.UserDTO;
import com.example.demo.dto.user.avatar.UserAvatarDTO;
import com.example.demo.enums.ErrorMessage;
import com.example.demo.mapper.friend.FriendRequestMapper;
import com.example.demo.mapper.friend.FriendShipMapper;
import com.example.demo.mapper.user.*;
import com.example.demo.model.friend.FriendRequest;
import com.example.demo.model.friend.Friendship;
import com.example.demo.model.user.*;
import com.example.demo.repository.FriendRequestRepository;
import com.example.demo.repository.FriendShipRepository;
import com.example.demo.repository.user.UserAvatarRepository;
import com.example.demo.repository.user.UserRepository;
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
    private final UserRepository userRepository;
    private final FriendRequestRepository friendRequestRepository;
    private final FriendShipRepository friendShipRepository;
    private final UserAvatarRepository userAvatarRepository;

    @Override
    public List<FriendRequestDTO> getFriendRequests(int limit) {
        // Get user id from token
        UUID userId = tokenHandler.getUserId();

        // Get friend requests for the user
        List<FriendRequest> friendRequests = friendRequestRepository.findByReceiverId(userId);

        if (limit != -1 && friendRequests.size() > limit) {
            friendRequests = friendRequests.subList(0, limit);
        }

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
    public FriendRequestDTO getFriendRequestById(UUID requestId) {
        // Get user id from token
        UUID userId = tokenHandler.getUserId();

        // Get friend request by id
        Optional<FriendRequest> friendRequest = friendRequestRepository.findById(requestId);
        if (friendRequest.isEmpty()) {
            throw new NotFoundException(ErrorMessage.FRIEND_REQUEST_NOT_FOUND);
        }

        // Fetch user profile for sender
        UserDTO userProfile = keycloakService.getUserProfileById(realmName, String.valueOf(friendRequest.get().getSender().getId()));
        // Fetch user profile for receiver
        UserDTO receiverProfile = keycloakService.getUserProfileById(realmName, String.valueOf(friendRequest.get().getReceiver().getId()));

        // Update sender details in friend request DTO
        FriendRequestDTO friendRequestDTO = FriendRequestMapper.INSTANCE.toDto(friendRequest.get());
        friendRequestDTO.getSender().setFirstName(userProfile.getFirstName());
        friendRequestDTO.getSender().setLastName(userProfile.getLastName());
        friendRequestDTO.getReceiver().setFirstName(receiverProfile.getFirstName());
        friendRequestDTO.getReceiver().setLastName(receiverProfile.getLastName());

        return friendRequestDTO;
    }

    @Override
    public List<RecommendDTO> getRecommendedFriends(int limit) {
        // Get user id from token
        UUID userId = tokenHandler.getUserId();

        List<User> users = userRepository.findUsersExcludingId(userId);

        if (limit != -1 && users.size() > limit) {
            users = users.subList(0, limit);
        }

        // Collect sender ids for batch retrieval
        List<String> userIds = users.stream()
                .map(request -> String.valueOf(request.getId()))
                .collect(Collectors.toList());

        // Fetch user profiles for senders
        Map<String, RecommendDTO> userProfileMap = keycloakService.getUserRecommendByIds(realmName, userIds);

        // Update sender details in friend request DTOs
        List<RecommendDTO> recommendDTOS = new ArrayList<>();
        userProfileMap.forEach((key, value) -> {
            UserRecommendDTO userRecommendDTO = buildUserDTO(value.getUser(), UUID.fromString(key));
            userRecommendDTO.setFirstName(value.getUser().getFirstName());
            userRecommendDTO.setLastName(value.getUser().getLastName());
            userRecommendDTO.setAvatar(value.getUser().getAvatar());

            if (value.getRequestId() != null) {
                FriendRequestDTO friendRequestDTO = getFriendRequestById(value.getRequestId());
                value.setRequest(friendRequestDTO);
            }

            RecommendDTO recommendDTO;
            if (value.getRequest() != null && (value.getRequest().getSender().getId().equals(userId) || value.getRequest().getReceiver().getId().equals(userId))) {
                recommendDTO = RecommendDTO.builder()
                        .user(userRecommendDTO)
                        .requestId(value.getRequestId())
                        .request(value.getRequest())
                        .isFriend(value.isFriend())
                        .build();

            } else {
                recommendDTO = RecommendDTO.builder()
                        .user(userRecommendDTO)
                        .requestId(null)
                        .request(null)
                        .isFriend(value.isFriend())
                        .build();

            }
            recommendDTOS.add(recommendDTO);
        });

        return recommendDTOS;
    }

    private UserRecommendDTO buildUserDTO(UserRecommendDTO userRecommendDTO, UUID userId) {
        Optional<UserAvatar> userAvatar = Optional.ofNullable(userAvatarRepository.findByUserId(userId));
        if (userAvatar.isEmpty()) {
            throw new NotFoundException(ErrorMessage.USER_AVATAR_NOT_FOUND);
        }
        UserAvatarDTO userAvatarDTO = UserAvatarMapper.INSTANCE.toDto(userAvatar.get());
        userRecommendDTO.setAvatar(userAvatarDTO);
        return userRecommendDTO;
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
        FriendShipCreateDTO addFriendToFriendListDTO = FriendShipCreateDTO.builder()
                .userId(friendRequest.get().getReceiver().getId())
                .friendId(friendRequest.get().getSender().getId())
                .build();
        Friendship addFriendToFriendList = FriendShipMapper.INSTANCE.toEntity(addFriendToFriendListDTO);
        addFriendToFriendList.setCreatedAt(new Date());
        addFriendToFriendList.setUpdatedAt(new Date());
        friendShipRepository.save(addFriendToFriendList);

        // Add user to friend's friend list
        FriendShipCreateDTO addUserToFriendListDTO = FriendShipCreateDTO.builder()
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

    @Override
    public ResponseDTO deleteFriendRequest(UUID requestId) {
        // Get user id from token
        UUID userId = tokenHandler.getUserId();

        // Check if friend request exists
        Optional<FriendRequest> friendRequest = friendRequestRepository.findById(requestId);
        if (friendRequest.isEmpty()) {
            throw new IllegalArgumentException(ErrorMessage.FRIEND_REQUEST_NOT_FOUND);
        }

        // Delete friend request
        friendRequestRepository.delete(friendRequest.get());

        return ResponseDTO.builder()
                .message("Friend request was deleted successfully")
                .build();
    }
}
