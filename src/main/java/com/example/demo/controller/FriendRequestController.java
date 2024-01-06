package com.example.demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/friend-request")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
public class FriendRequestController {

    @RequestMapping
    public ResponseEntity<String> getFriendRequests() {
        return null;
    }

    @RequestMapping("/send")
    public ResponseEntity<String> sendFriendRequest() {
        return null;
    }

    public ResponseEntity<String> acceptFriendRequest() {
        return null;
    }
}
