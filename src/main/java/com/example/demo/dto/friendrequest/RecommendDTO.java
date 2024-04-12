package com.example.demo.dto.friendrequest;

import com.example.demo.dto.user.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class RecommendDTO {
    private UserRecommendDTO user;
    private UUID requestId;
    private FriendRequestDTO request;
    private boolean isFriend;
}
