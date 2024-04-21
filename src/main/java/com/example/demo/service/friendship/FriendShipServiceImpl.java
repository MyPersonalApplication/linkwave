package com.example.demo.service.friendship;

import com.example.demo.config.authentication.TokenHandler;
import com.example.demo.dto.ResponseDTO;
import com.example.demo.dto.friendrequest.RecommendDTO;
import com.example.demo.dto.friendship.FriendShipDTO;
import com.example.demo.dto.user.UserDTO;
import com.example.demo.dto.user.skill.UserSkillDTO;
import com.example.demo.mapper.friend.FriendShipMapper;
import com.example.demo.mapper.user.UserSkillMapper;
import com.example.demo.model.friend.Friendship;
import com.example.demo.repository.FriendShipRepository;
import com.example.demo.service.keycloak.KeycloakService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FriendShipServiceImpl implements FriendShipService {
    private final String realmName = "linkwave";
    private final KeycloakService keycloakService;
    private final TokenHandler tokenHandler;
    private final FriendShipRepository friendShipRepository;

    @Override
    public List<FriendShipDTO> getFriendList() {
        // Get user id from token
        UUID userId = tokenHandler.getUserId();

        // Get friend list
        List<Friendship> friendShips = friendShipRepository.findByUserId(userId);

        // Convert to DTO
        List<FriendShipDTO> friendShipDTOS = friendShips.stream().map(FriendShipMapper.INSTANCE::toDto).toList();

        // Collect sender ids for batch retrieval
        List<String> userIds = friendShipDTOS.stream()
                .map(request -> String.valueOf(request.getUser().getId()))
                .collect(Collectors.toList());

        // Fetch user profiles
        Map<String, UserDTO> userProfileMap = keycloakService.getUserProfilesByIds(realmName, userIds);

        friendShipDTOS.forEach(friendShipDTO -> {
            UserDTO userDTO = userProfileMap.get(String.valueOf(friendShipDTO.getUser().getId()));
            friendShipDTO.getUser().setFirstName(userDTO.getFirstName());
            friendShipDTO.getUser().setLastName(userDTO.getLastName());
            friendShipDTO.getUser().setEmail(userDTO.getEmail());
        });

        return friendShipDTOS;
    }

    @Override
    public ResponseDTO unfriend(UUID friendId) {
        // Get user id from token
        UUID userId = tokenHandler.getUserId();

        // Unfriend user
        Friendship friendship = friendShipRepository.findByUserIdAndFriendId(userId, friendId);
        friendShipRepository.deleteById(friendship.getId());

        // Unfriend friend
        friendship = friendShipRepository.findByUserIdAndFriendId(friendId, userId);
        friendShipRepository.deleteById(friendship.getId());

        return ResponseDTO.builder().message("Unfriended successfully").build();
    }
}
