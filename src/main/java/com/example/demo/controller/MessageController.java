package com.example.demo.controller;

import com.example.demo.dto.ResponseDTO;
import com.example.demo.dto.message.CreateMessageDTO;
import com.example.demo.dto.message.MarkAsReadDTO;
import com.example.demo.dto.message.MessageAttachmentDTO;
import com.example.demo.dto.message.MessageDTO;
import com.example.demo.dto.postmedia.PostMediaDTO;
import com.example.demo.service.message.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
public class MessageController {
    private final MessageService messageService;

    @PostMapping()
    public ResponseEntity<Void> createMessage(
            @RequestParam("content") String content,
            @RequestParam("conversationId") UUID conversationId,
            @RequestPart("multipartFiles") List<MultipartFile> multipartFiles) {

        // Filter out empty MultipartFile instances
        List<MultipartFile> validMultipartFiles = multipartFiles.stream()
                .filter(file -> file != null && !file.isEmpty())
                .toList();

        CreateMessageDTO createMessageDTO = CreateMessageDTO.builder()
                .content(content)
                .conversationId(conversationId)
                .multipartFiles(validMultipartFiles)
                .build();
        messageService.createMessage(createMessageDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{messageId}")
    public ResponseEntity<MessageDTO> getMessage(@PathVariable UUID messageId) {
        return ResponseEntity.ok(messageService.getMessage(messageId));
    }

    @PutMapping("/mark-as-read")
    public ResponseEntity<ResponseDTO> markAsRead(@RequestBody MarkAsReadDTO markAsReadDTO) {
        return ResponseEntity.ok(messageService.markAsRead(markAsReadDTO.getListMessageIds()));
    }

    @PostMapping("/{messageId}/attachment")
    public ResponseEntity<List<MessageAttachmentDTO>> createMessageAttachment(@RequestPart("multipartFile") List<MultipartFile> multipartFiles, @PathVariable UUID messageId) {
        return ResponseEntity.ok(messageService.createMessageAttachment(multipartFiles, messageId));
    }
}
