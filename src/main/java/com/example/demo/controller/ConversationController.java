package com.example.demo.controller;

import com.example.demo.dto.ResponseDTO;
import com.example.demo.dto.conversation.ConversationDTO;
import com.example.demo.dto.conversation.CreateConversationDTO;
import com.example.demo.service.conversation.ConversationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/conversations")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
public class ConversationController {
    private final ConversationService conversationService;

    @PostMapping()
    public ResponseEntity<ConversationDTO> createConversation(@RequestBody CreateConversationDTO createConversationDTO) {
        return ResponseEntity.ok(conversationService.createConversation(createConversationDTO.getFriendId()));
    }

    @GetMapping()
    public ResponseEntity<List<ConversationDTO>> getConversations() {
        return ResponseEntity.ok(conversationService.getConversations());
    }

    @GetMapping("/{conversationId}")
    public ResponseEntity<ConversationDTO> getConversation(@PathVariable UUID conversationId) {
        return ResponseEntity.ok(conversationService.getConversation(conversationId));
    }

    @DeleteMapping("/{conversationId}")
    public ResponseEntity<Void> deleteConversation(@PathVariable UUID conversationId) {
        conversationService.deleteConversation(conversationId);
        return ResponseEntity.ok().build();
    }
}
