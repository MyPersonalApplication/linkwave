package com.example.demo.controller;

import com.example.demo.dto.message.MessageDTO;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {
    
    @MessageMapping("/message")
    @SendTo("/topic/chat")
    public MessageDTO handleMessage(@Payload MessageDTO message) {
        return message;
    }
}
