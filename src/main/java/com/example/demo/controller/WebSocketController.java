package com.example.demo.controller;

import com.example.demo.dto.message.CreateMessageDTO;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {
    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/chat")
    public String sendMessage(@Payload String messageId) {
        return messageId;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/chat")
    public CreateMessageDTO addUser(@Payload CreateMessageDTO createMessageDTO, SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", createMessageDTO.getSenderId());
        return createMessageDTO;
    }
}
