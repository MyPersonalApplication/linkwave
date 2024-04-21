package com.example.demo.service.message;

import com.example.demo.config.authentication.TokenHandler;
import com.example.demo.controller.exception.NotFoundException;
import com.example.demo.dto.ResponseDTO;
import com.example.demo.dto.friendrequest.UserRecommendDTO;
import com.example.demo.dto.message.CreateMessageDTO;
import com.example.demo.dto.message.MessageDTO;
import com.example.demo.dto.user.UserDTO;
import com.example.demo.dto.user.avatar.UserAvatarDTO;
import com.example.demo.enums.ErrorMessage;
import com.example.demo.mapper.MessageMapper;
import com.example.demo.mapper.user.UserAvatarMapper;
import com.example.demo.model.chat.Message;
import com.example.demo.model.user.UserAvatar;
import com.example.demo.repository.MessageRepository;
import com.example.demo.repository.user.UserAvatarRepository;
import com.example.demo.service.keycloak.KeycloakService;
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
    private final KeycloakService keycloakService;
    private final String realmName = "linkwave";
    private final TokenHandler tokenHandler;
    private final MessageRepository messageRepository;
    private final UserService userService;
    private final UserAvatarRepository userAvatarRepository;

    @Override
    public MessageDTO createMessage(CreateMessageDTO createMessageDTO) {
        // Get user id from token
        UUID userId = tokenHandler.getUserId();

        createMessageDTO.setSenderId(userId);
        Message message = MessageMapper.INSTANCE.toEntity(createMessageDTO);
        message.setIsRead(false);
        message.setCreatedAt(new Date());
        message.setUpdatedAt(new Date());

        messageRepository.save(message);

        // Get message dto
        UserDTO userDTO = buildUserDTO(userId);
        MessageDTO messageDTO = MessageMapper.INSTANCE.toDto(message);
        messageDTO.setSender(userDTO);

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
    public List<MessageDTO> getMessages(UUID conversationId) {
        // Get list of messages
        List<Message> messages = messageRepository.findByConversationId(conversationId);
        List<MessageDTO> messageDTOS = messages.stream().map(MessageMapper.INSTANCE::toDto).toList();

        messageDTOS.forEach(messageDTO -> {
            UserDTO userDTO = buildUserDTO(messageDTO.getSender().getId());
            messageDTO.setSender(userDTO);
        });

        return messageDTOS;
    }

    private UserDTO buildUserDTO(UUID userId) {
        UserDTO userDTO = keycloakService.getUserProfileById(realmName, String.valueOf(userId));
        Optional<UserAvatar> userAvatar = Optional.ofNullable(userAvatarRepository.findByUserId(userId));
        if (userAvatar.isEmpty()) {
            throw new NotFoundException(ErrorMessage.USER_AVATAR_NOT_FOUND);
        }
        UserAvatarDTO userAvatarDTO = UserAvatarMapper.INSTANCE.toDto(userAvatar.get());
        userDTO.setAvatar(userAvatarDTO);
        return userDTO;
    }
}
