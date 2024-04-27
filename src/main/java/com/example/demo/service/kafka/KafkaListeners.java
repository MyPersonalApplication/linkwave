package com.example.demo.service.kafka;

import com.example.demo.dto.message.MessageDTO;
import com.example.demo.service.message.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class KafkaListeners {
    @Autowired
    private WebSocketMessageBroadcaster broadcaster;

    @Autowired
    private MessageService messageService;

    @KafkaListener(
            topics = "linkwave",
            groupId = "chat-group"
    )
    public void listen(UUID messageId) {
        System.out.println("Received message: " + messageId);
        MessageDTO message = messageService.getMessage(messageId);
        broadcaster.broadcastMessage(message);
    }
}
