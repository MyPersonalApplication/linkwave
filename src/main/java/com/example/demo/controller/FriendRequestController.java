package com.example.demo.controller;

import com.example.demo.dto.friendrequest.AcceptRequestDTO;
import com.example.demo.dto.friendrequest.FriendRequestDTO;
import com.example.demo.dto.friendrequest.SendRequestDTO;
import com.example.demo.dto.ResponseDTO;
import com.example.demo.dto.friendrequest.RejectRequestDTO;
import com.example.demo.service.friendrequest.FriendRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/friend-request")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
public class FriendRequestController {
    private final FriendRequestService friendRequestService;

    @GetMapping
    public ResponseEntity<List<FriendRequestDTO>> getFriendRequests() {
        List<FriendRequestDTO> friendRequestDTO = friendRequestService.getFriendRequests();
        return ResponseEntity.ok(friendRequestDTO);
    }

    @PostMapping()
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
