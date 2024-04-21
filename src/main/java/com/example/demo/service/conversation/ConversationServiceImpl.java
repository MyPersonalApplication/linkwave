package com.example.demo.service.conversation;

import com.example.demo.config.authentication.TokenHandler;
import com.example.demo.dto.ResponseDTO;
import com.example.demo.dto.conversation.ConversationDTO;
import com.example.demo.dto.conversation.CreateConversationDTO;
import com.example.demo.dto.friendship.FriendShipDTO;
import com.example.demo.dto.message.MessageDTO;
import com.example.demo.dto.participant.CreateParticipantDTO;
import com.example.demo.dto.participant.ParticipantDTO;
import com.example.demo.mapper.ConversationMapper;
import com.example.demo.mapper.MessageMapper;
import com.example.demo.mapper.ParticipantMapper;
import com.example.demo.mapper.friend.FriendShipMapper;
import com.example.demo.model.chat.Conversation;
import com.example.demo.model.chat.Message;
import com.example.demo.model.chat.Participant;
import com.example.demo.repository.ConversationRepository;
import com.example.demo.repository.MessageRepository;
import com.example.demo.repository.ParticipantRepository;
import com.example.demo.service.keycloak.KeycloakService;
import com.example.demo.service.message.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConversationServiceImpl implements ConversationService {
    private final String realmName = "linkwave";
    private final TokenHandler tokenHandler;
    private final ConversationRepository conversationRepository;
    private final ParticipantRepository participantRepository;
    private final MessageService messageService;
    private final MessageRepository messageRepository;

    @Override
    public ConversationDTO createConversation(UUID friendId) {
        // Get user id from token
        UUID userId = tokenHandler.getUserId();

        // Check if conversation exist
        Participant participant = participantRepository.isExistConversation(friendId, userId);

        if (participant != null) {
            return getConversation(participant.getConversation().getId());
        }

        // Create conversation
        Conversation conversation = Conversation.builder()
                .messages(List.of())
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();
        conversationRepository.save(conversation);

        // Create participant
        CreateParticipantDTO createParticipantUser = CreateParticipantDTO.builder()
                .conversationId(conversation.getId())
                .userId(userId)
                .build();
        Participant createParticipant = ParticipantMapper.INSTANCE.mapFromCreate(createParticipantUser);
        participantRepository.save(createParticipant);

        CreateParticipantDTO createParticipantFriend = CreateParticipantDTO.builder()
                .conversationId(conversation.getId())
                .userId(friendId)
                .build();
        Participant createParticipantFriendEntity = ParticipantMapper.INSTANCE.mapFromCreate(createParticipantFriend);
        participantRepository.save(createParticipantFriendEntity);

        return ConversationMapper.INSTANCE.toDto(conversation);
    }

    @Override
    public List<ConversationDTO> getConversations() {
        // Get user id from token
        UUID userId = tokenHandler.getUserId();

        List<Participant> participants = participantRepository.findByUserId(userId);
        List<ConversationDTO> conversationDTOS = participants.stream().map(Participant::getConversation).map(ConversationMapper.INSTANCE::toDto).toList();

        conversationDTOS.forEach(conversationDTO -> {
            List<MessageDTO> messageDTOS = messageService.getMessages(conversationDTO.getId());
            conversationDTO.setMessages(messageDTOS);
        });

        return conversationDTOS;
    }

    @Override
    public ConversationDTO getConversation(UUID conversationId) {
        Conversation conversation = conversationRepository.findById(conversationId).orElseThrow();
        ConversationDTO conversationDTO = ConversationMapper.INSTANCE.toDto(conversation);
        conversationDTO.setMessages(messageService.getMessages(conversationId));
        return conversationDTO;
    }
}
