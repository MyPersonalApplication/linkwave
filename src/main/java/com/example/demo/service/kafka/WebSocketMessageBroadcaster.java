package com.example.demo.service.kafka;

import com.example.demo.dto.message.MessageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WebSocketMessageBroadcaster {
    private final SimpMessagingTemplate messagingTemplate;

    public void broadcastMessage(MessageDTO message) {
        messagingTemplate.convertAndSend("/topic/chat", message);
    }
}
