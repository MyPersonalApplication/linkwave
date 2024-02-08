package com.example.demo.controller;

import com.example.demo.dto.friendrequest.AcceptRequestDTO;
import com.example.demo.dto.friendrequest.SendRequestDTO;
import com.example.demo.dto.ResponseDTO;
import com.example.demo.dto.friendrequest.RejectRequestDTO;
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
    public ResponseEntity<ResponseDTO> sendFriendRequest(@RequestBody SendRequestDTO sendRequestDTO) {
        ResponseDTO responseDTO = friendRequestService.sendFriendRequest(sendRequestDTO.getReceiverId());
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/accept")
    public ResponseEntity<ResponseDTO> acceptFriendRequest(@RequestBody AcceptRequestDTO acceptRequestDTO) {
        ResponseDTO responseDTO = friendRequestService.acceptFriendRequest(acceptRequestDTO.getRequestId());
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/reject")
    public ResponseEntity<ResponseDTO> rejectFriendRequest(@RequestBody RejectRequestDTO rejectRequestDTO) {
        ResponseDTO responseDTO = friendRequestService.rejectFriendRequest(rejectRequestDTO.getRequestId());
        return ResponseEntity.ok(responseDTO);
    }
}
