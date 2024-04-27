package com.example.demo.service.message;

import com.example.demo.config.authentication.TokenHandler;
import com.example.demo.controller.exception.NotFoundException;
import com.example.demo.dto.ResponseDTO;
import com.example.demo.dto.message.CreateMessageDTO;
import com.example.demo.dto.message.MessageDTO;
import com.example.demo.dto.user.UserDTO;
import com.example.demo.enums.ErrorMessage;
import com.example.demo.mapper.MessageMapper;
import com.example.demo.model.chat.Conversation;
import com.example.demo.model.chat.Message;
import com.example.demo.repository.ConversationRepository;
import com.example.demo.repository.MessageRepository;
import com.example.demo.service.kafka.KafkaProducer;
import com.example.demo.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageServiceImpl implements MessageService {
    private final TokenHandler tokenHandler;
    private final MessageRepository messageRepository;
    private final ConversationRepository conversationRepository;
    private final UserService userService;
    private final KafkaProducer kafkaProducer;

    @Override
    public MessageDTO createMessage(CreateMessageDTO createMessageDTO) {
        // Get user id from token
        UUID userId = tokenHandler.getUserId();

        if (createMessageDTO.getSenderId() == null) {
            createMessageDTO.setSenderId(userId);
        }
        Message message = MessageMapper.INSTANCE.toEntity(createMessageDTO);
        message.setIsRead(false);
        message.setCreatedAt(new Date());
        message.setUpdatedAt(new Date());

        messageRepository.save(message);

        // Update conversation
        Conversation conversation = conversationRepository.findById(createMessageDTO.getConversationId())
                .orElseThrow(() -> new NotFoundException(ErrorMessage.CONVERSATION_NOT_FOUND));
        conversation.setUpdatedAt(new Date());
        conversationRepository.save(conversation);

        // Get message dto
        MessageDTO messageDTO = getMessage(message.getId());

        // Send message to kafka
        kafkaProducer.sendMessage(messageDTO.getId().toString());

        return messageDTO;
    }

    @Override
    public ResponseDTO markAsRead(List<UUID> listMessageIds) {
        // Get list of messages
        List<Message> messages = messageRepository.findByIds(listMessageIds);

        messages.forEach(message -> {
            message.setIsRead(true);
            messageRepository.save(message);
        });

        return ResponseDTO.builder().message("Mark as read successfully").build();
    }

    @Override
    public MessageDTO getMessage(UUID messageId) {
        // Get user id from token
        UUID userId = tokenHandler.getUserId();

        // Get message
        Optional<Message> message = messageRepository.findById(messageId);

        if (message.isEmpty()) {
            throw new NotFoundException(ErrorMessage.MESSAGE_NOT_FOUND);
        }

        UserDTO userDTO = userService.buildUserDTO(userId);
        MessageDTO messageDTO = MessageMapper.INSTANCE.toDto(message.get());
        messageDTO.setSender(userDTO);

        return messageDTO;
    }

    @Override
    public List<MessageDTO> getMessages(UUID conversationId) {
        // Get list of messages
        List<Message> messages = messageRepository.findByConversationId(conversationId);
        List<MessageDTO> messageDTOS = messages.stream().map(MessageMapper.INSTANCE::toDto).toList();

        messageDTOS.forEach(messageDTO -> {
            UUID userId = messageDTO.getSender().getId();
            UserDTO userDTO = userService.buildUserDTO(userId);
            messageDTO.setSender(userDTO);
        });

        return messageDTOS;
    }
}
