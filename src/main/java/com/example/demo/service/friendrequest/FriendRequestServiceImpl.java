package com.example.demo.service.friendrequest;

import com.example.demo.config.authentication.TokenHandler;
import com.example.demo.dto.ResponseDTO;
import com.example.demo.enums.ErrorMessage;
import com.example.demo.model.user.User;
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

    @Override
    public ResponseDTO sendFriendRequest(UUID receiverId) {
        UUID userId = tokenHandler.getUserId();
        if (userId.equals(receiverId)) {
            throw new IllegalArgumentException(ErrorMessage.CANNOT_REQUEST_TO_YOURSELF);
        }
        Optional<User> receiver = Optional.ofNullable(userService.loadUserByUsername(receiverId.toString()));
        if (receiver.isEmpty()) {
            throw new IllegalArgumentException(ErrorMessage.USER_NOT_FOUND);
        } else {
            log.info(userId + " sent friend request to " + receiverId);
        }
        return ResponseDTO.builder()
                .message(userId + " sent friend request to " + receiverId)
                .build();
    }
}
