package com.example.demo.controller;

import com.example.demo.dto.base.SearchResultDTO;
import com.example.demo.dto.friendrequest.FriendRequestDTO;
import com.example.demo.dto.friendrequest.RecommendDTO;
import com.example.demo.dto.friendrequest.SendRequestDTO;
import com.example.demo.dto.ResponseDTO;
import com.example.demo.dto.user.UserDTO;
import com.example.demo.service.friendrequest.FriendRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/friends")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
public class FriendRequestController {
    private final FriendRequestService friendRequestService;

    @GetMapping("/requests")
    public ResponseEntity<List<FriendRequestDTO>> getFriendRequests(
            @RequestParam(required = false, defaultValue = "-1") int limit
    ) {
        List<FriendRequestDTO> friendRequestDTO = friendRequestService.getFriendRequests(limit);
        return ResponseEntity.ok(friendRequestDTO);
    }

    @DeleteMapping("/delete/{requestId}")
    public ResponseEntity<ResponseDTO> deleteFriendRequest(@PathVariable UUID requestId) {
        ResponseDTO responseDTO = friendRequestService.deleteFriendRequest(requestId);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/search")
    public ResponseEntity<SearchResultDTO> searchFriend(@RequestParam String query,
                                                        @RequestParam(required = false, defaultValue = "0") int page,
                                                        @RequestParam(required = false, defaultValue = "10") int pageSize) {
        return ResponseEntity.ok(friendRequestService.searchFriend(query, page, pageSize));
    }

    @GetMapping("/recommends")
    public ResponseEntity<List<RecommendDTO>> getRecommendedFriends(
            @RequestParam(required = false, defaultValue = "-1") int limit
    ) {
        List<RecommendDTO> recommendedFriends = friendRequestService.getRecommendedFriends(limit);
        return ResponseEntity.ok(recommendedFriends);
    }

    @PostMapping("/send")
    public ResponseEntity<ResponseDTO> sendFriendRequest(@RequestBody SendRequestDTO sendRequestDTO) {
        ResponseDTO responseDTO = friendRequestService.sendFriendRequest(sendRequestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping("/accept/{requestId}")
    public ResponseEntity<ResponseDTO> acceptFriendRequest(@PathVariable UUID requestId) {
        ResponseDTO responseDTO = friendRequestService.acceptFriendRequest(requestId);
        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping("/reject/{requestId}")
    public ResponseEntity<ResponseDTO> rejectFriendRequest(@PathVariable UUID requestId) {
        ResponseDTO responseDTO = friendRequestService.rejectFriendRequest(requestId);
        return ResponseEntity.ok(responseDTO);
    }
}
