package com.example.demo.controller;

import com.example.demo.dto.message.MessageDTO;
import com.example.demo.dto.post.PostDTO;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {
    
    @MessageMapping("/chat")
    @SendTo("/topic/chat")
    public MessageDTO handleMessage(@Payload MessageDTO message) {
        return message;
    }

    @MessageMapping("/post")
    @SendTo("/topic/post")
    public PostDTO handlePost(@Payload PostDTO post) {
        return post;
    }
}
