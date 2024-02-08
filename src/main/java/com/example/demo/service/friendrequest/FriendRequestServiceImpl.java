package com.example.demo.service.friendrequest;

import com.example.demo.config.authentication.TokenHandler;
import com.example.demo.dto.ResponseDTO;
import com.example.demo.enums.ErrorMessage;
import com.example.demo.model.friend.FriendRequest;
import com.example.demo.model.friend.Friendship;
import com.example.demo.model.user.User;
import com.example.demo.repository.FriendRequestRepository;
import com.example.demo.repository.FriendShipRepository;
import com.example.demo.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class FriendRequestServiceImpl implements FriendRequestService {
    private final TokenHandler tokenHandler;
    private final UserService userService;
    private final FriendRequestRepository friendRequestRepository;
    private final FriendShipRepository friendShipRepository;

    @Override
    public ResponseDTO sendFriendRequest(UUID receiverId) {
        // Get user id from token
        UUID userId = tokenHandler.getUserId();

        // Check if sender and receiver are the same
        if (userId.equals(receiverId)) {
            throw new IllegalArgumentException(ErrorMessage.CANNOT_REQUEST_TO_YOURSELF);
        }
        Optional<User> sender = Optional.ofNullable(userService.loadUserById(userId));
        if (sender.isEmpty()) {
            throw new IllegalArgumentException(ErrorMessage.USER_NOT_FOUND);
        }

        // Check if receiver exists
        Optional<User> receiver = Optional.ofNullable(userService.loadUserById(receiverId));
        if (receiver.isEmpty()) {
            throw new IllegalArgumentException(ErrorMessage.USER_NOT_FOUND);
        } else {
            log.info(userId + " sent friend request to " + receiverId);
        }

        // Check if friend request already exists
        Optional<FriendRequest> existFriendRequestWasSend = Optional.ofNullable(friendRequestRepository.findBySenderIdAndReceiverId(userId, receiverId));
        Optional<FriendRequest> existFriendRequestWasReceived = Optional.ofNullable(friendRequestRepository.findBySenderIdAndReceiverId(receiverId, userId));
        if (existFriendRequestWasSend.isPresent() || existFriendRequestWasReceived.isPresent()) {
            throw new IllegalArgumentException(ErrorMessage.FRIEND_REQUEST_ALREADY_EXISTS);
        }

        // Send friend request
        FriendRequest friendRequest = FriendRequest.builder()
                .sender(sender.get())
                .receiver(receiver.get())
                .build();
        friendRequestRepository.save(friendRequest);

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
        Friendship addFriendToFriendList = Friendship.builder()
                .user(friendRequest.get().getReceiver())
                .friend(friendRequest.get().getSender())
                .build();
        friendShipRepository.save(addFriendToFriendList);

        // Add user to friend's friend list
        Friendship addUserToFriendList = Friendship.builder()
                .user(friendRequest.get().getSender())
                .friend(friendRequest.get().getReceiver())
                .build();
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
