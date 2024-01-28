package com.example.demo.controller;

import com.example.demo.dto.FriendRequestDTO;
import com.example.demo.dto.ResponseDTO;
import com.example.demo.service.friendrequest.FriendRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/friend-request")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
public class FriendRequestController {
    private final FriendRequestService friendRequestService;

    @GetMapping
    public ResponseEntity<String> getFriendRequests() {
        return null;
    }

    @PostMapping("/send")
    public ResponseEntity<ResponseDTO> sendFriendRequest(@RequestBody FriendRequestDTO friendRequestDTO) {
        ResponseDTO responseDTO = friendRequestService.sendFriendRequest(friendRequestDTO.getReceiverId());
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/accept")
    public ResponseEntity<String> acceptFriendRequest() {
        return null;
    }

    @PostMapping("/reject")
    public ResponseEntity<String> rejectFriendRequest() {
        return null;
    }
}
