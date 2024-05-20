package com.example.demo.service.message;

import com.example.demo.dto.ResponseDTO;
import com.example.demo.dto.conversation.ConversationDTO;
import com.example.demo.dto.message.CreateMessageDTO;
import com.example.demo.dto.message.MessageAttachmentDTO;
import com.example.demo.dto.message.MessageDTO;
import com.example.demo.dto.postmedia.PostMediaDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    void createMessage(CreateMessageDTO createMessageDTO);

    ResponseDTO markAsRead(List<UUID> listMessageIds);

    MessageDTO getMessage(UUID messageId);

    List<MessageDTO> getMessages(UUID conversationId);

    List<MessageAttachmentDTO> createMessageAttachment(List<MultipartFile> multipartFiles, UUID messageId);
}
