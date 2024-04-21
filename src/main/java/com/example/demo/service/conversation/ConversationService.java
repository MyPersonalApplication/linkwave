package com.example.demo.service.conversation;

import com.example.demo.dto.conversation.ConversationDTO;

import java.util.List;
import java.util.UUID;

public interface ConversationService {
    ConversationDTO createConversation(UUID friendId);

    List<ConversationDTO> getConversations();

    ConversationDTO getConversation(UUID conversationId);
}
