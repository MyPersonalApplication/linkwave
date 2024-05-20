package com.example.demo.service.message;

import com.example.demo.config.authentication.TokenHandler;
import com.example.demo.controller.exception.InvalidDataException;
import com.example.demo.controller.exception.NotFoundException;
import com.example.demo.dto.ResponseDTO;
import com.example.demo.dto.message.CreateMessageAttachmentDTO;
import com.example.demo.dto.message.CreateMessageDTO;
import com.example.demo.dto.message.MessageAttachmentDTO;
import com.example.demo.dto.message.MessageDTO;
import com.example.demo.dto.postmedia.CreatePostMediaDTO;
import com.example.demo.dto.user.UserDTO;
import com.example.demo.enums.ErrorMessage;
import com.example.demo.mapper.MessageAttachmentMapper;
import com.example.demo.mapper.MessageMapper;
import com.example.demo.mapper.PostMediaMapper;
import com.example.demo.model.chat.Conversation;
import com.example.demo.model.chat.Message;
import com.example.demo.model.chat.MessageAttachment;
import com.example.demo.model.interact.PostMedia;
import com.example.demo.repository.ConversationRepository;
import com.example.demo.repository.MessageAttachmentRepository;
import com.example.demo.repository.MessageRepository;
import com.example.demo.service.cloudinary.CloudinaryService;
import com.example.demo.service.kafka.KafkaProducer;
import com.example.demo.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageServiceImpl implements MessageService {
    private final TokenHandler tokenHandler;
    private final MessageRepository messageRepository;
    private final ConversationRepository conversationRepository;
    private final UserService userService;
    private final KafkaProducer kafkaProducer;
    private final CloudinaryService cloudinaryService;
    private final Environment environment;
    private final MessageAttachmentRepository messageAttachmentRepository;

    @Override
    public void createMessage(CreateMessageDTO createMessageDTO) {
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
//        MessageDTO messageDTO = getMessage(message.getId());

        if (!createMessageDTO.getMultipartFiles().isEmpty()) {
            createMessageAttachment(createMessageDTO.getMultipartFiles(), message.getId());
//            messageDTO.setLstAttachments(messageAttachmentDTOS);
        }

        // Send message to kafka
        kafkaProducer.sendMessage(message.getId().toString());
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
    @Transactional(readOnly = true)
    public MessageDTO getMessage(UUID messageId) {
        // Get user id from token
        UUID userId = tokenHandler.getUserId();

        // Get message
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.MESSAGE_NOT_FOUND));
        Hibernate.initialize(message.getAttachments());

        UserDTO userDTO = userService.buildUserDTO(userId);
        MessageDTO messageDTO = MessageMapper.INSTANCE.toDto(message);
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

    @Override
    public List<MessageAttachmentDTO> createMessageAttachment(List<MultipartFile> multipartFiles, UUID messageId) {
        return multipartFiles.stream()
                .map(multipartFile -> {
                    try {
                        return processSaveImage(multipartFile, messageId);
                    } catch (IOException e) {
                        throw new InvalidDataException(ErrorMessage.FAILED_TO_SAVE_IMAGE);
                    }
                })
                .toList();
    }

    private MessageAttachmentDTO processSaveImage(MultipartFile multipartFile, UUID messageId) throws IOException {
        String[] activeProfiles = environment.getActiveProfiles();
        Map result = cloudinaryService.uploadFile(multipartFile, (activeProfiles.length == 0 ? "prod" : activeProfiles[0]) + "/message-attachment");

        CreateMessageAttachmentDTO createMessageAttachmentDTO = CreateMessageAttachmentDTO.builder()
                .fileUrl((String) result.get("url"))
                .fileId((String) result.get("public_id"))
                .fileName((String) result.get("original_filename"))
                .fileType(getFileExtension(multipartFile.getContentType()))
                .messageId(messageId)
                .build();

        MessageAttachment messageAttachment = MessageAttachmentMapper.INSTANCE.toEntity(createMessageAttachmentDTO);
        messageAttachment.setCreatedAt(new Date());
        messageAttachment.setUpdatedAt(new Date());
        messageAttachmentRepository.save(messageAttachment);

        return MessageAttachmentMapper.INSTANCE.toDto(messageAttachment);
    }

    // Function to map MIME type to file extension
    private String getFileExtension(String mimeType) {
        Map<String, String> mimeTypes = Map.of(
                MimeTypeUtils.IMAGE_PNG_VALUE, "PNG",
                MimeTypeUtils.IMAGE_JPEG_VALUE, "JPG",
                "application/pdf", "PDF",
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "DOCX",
                "text/plain", "TXT",
                "application/vnd.ms-excel", "XLS",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "XLSX"
        );

        return mimeTypes.getOrDefault(mimeType, "unknown");
    }
}
