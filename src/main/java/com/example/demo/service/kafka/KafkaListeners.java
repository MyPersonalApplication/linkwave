package com.example.demo.service.kafka;

import com.example.demo.dto.message.MessageDTO;
import com.example.demo.dto.post.PostDTO;
import com.example.demo.service.message.MessageService;
import com.example.demo.service.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class KafkaListeners {
    private final MessageService messageService;
    private final PostService postService;
    private final SimpMessagingTemplate messagingTemplate;

    @KafkaListener(
            topics = "chat-message",
            groupId = "linkwave-group"
    )
    public void listenMessage(UUID messageId) {
        System.out.println("Received message: " + messageId);

        MessageDTO message = messageService.getMessage(messageId);
        messagingTemplate.convertAndSend("/topic/chat", message);
    }

    @KafkaListener(
            topics = "post",
            groupId = "linkwave-group"
    )
    public void listenPost(UUID postId) {
        System.out.println("Received post: " + postId);

        PostDTO postDTO = postService.getPost(postId);
        messagingTemplate.convertAndSend("/topic/post", postDTO);
    }
}
