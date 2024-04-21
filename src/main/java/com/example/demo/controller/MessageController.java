package com.example.demo.controller;

import com.example.demo.dto.ResponseDTO;
import com.example.demo.dto.conversation.ConversationDTO;
import com.example.demo.dto.message.CreateMessageDTO;
import com.example.demo.dto.message.MarkAsReadDTO;
import com.example.demo.dto.message.MessageDTO;
import com.example.demo.service.message.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
public class MessageController {
    private final MessageService messageService;

    @PostMapping()
    public ResponseEntity<MessageDTO> createMessage(@RequestBody CreateMessageDTO createMessageDTO) {
        return ResponseEntity.ok(messageService.createMessage(createMessageDTO));
    }

    @PutMapping("/mark-as-read")
    public ResponseEntity<ResponseDTO> markAsRead(@RequestBody MarkAsReadDTO markAsReadDTO) {
        return ResponseEntity.ok(messageService.markAsRead(markAsReadDTO.getListMessageIds()));
    }
}
