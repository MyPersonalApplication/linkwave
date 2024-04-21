package com.example.demo.service.message;

import com.example.demo.dto.ResponseDTO;
import com.example.demo.dto.conversation.ConversationDTO;
import com.example.demo.dto.message.CreateMessageDTO;
import com.example.demo.dto.message.MessageDTO;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    MessageDTO createMessage(CreateMessageDTO createMessageDTO);

    ResponseDTO markAsRead(List<UUID> listMessageIds);

    List<MessageDTO> getMessages(UUID conversationId);
}
