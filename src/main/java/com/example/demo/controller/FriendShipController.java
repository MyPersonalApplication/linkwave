package com.example.demo.controller;

import com.example.demo.dto.ResponseDTO;
import com.example.demo.dto.friendrequest.FriendRequestDTO;
import com.example.demo.dto.friendship.FriendShipDTO;
import com.example.demo.service.friendship.FriendShipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/friendships")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
public class FriendShipController {
    private final FriendShipService friendShipService;

    @GetMapping
    public ResponseEntity<List<FriendShipDTO>> getFriendList(
            @RequestParam(required = false, defaultValue = "-1") int limit
    ) {
        List<FriendShipDTO> friendShipDTO = friendShipService.getFriendList(limit);
        return ResponseEntity.ok(friendShipDTO);
    }

    @DeleteMapping("/unfriend/{friendId}")
    public ResponseEntity<ResponseDTO> unfriend(@PathVariable UUID friendId) {
        return ResponseEntity.ok(friendShipService.unfriend(friendId));
    }
}
