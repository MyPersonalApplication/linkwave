package com.example.demo.service.participant;

import com.example.demo.config.authentication.TokenHandler;
import com.example.demo.dto.participant.ParticipantDTO;
import com.example.demo.dto.user.UserDTO;
import com.example.demo.mapper.ParticipantMapper;
import com.example.demo.model.chat.Participant;
import com.example.demo.repository.ParticipantRepository;
import com.example.demo.service.keycloak.KeycloakService;
import com.example.demo.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ParticipantServiceImpl implements ParticipantService {
    private final ParticipantRepository participantRepository;
    private final UserService userService;

    @Override
    public List<ParticipantDTO> getParticipants(UUID conversationId) {
        // Get list of participants
        List<Participant> participants = participantRepository.findByConversationId(conversationId);
        List<ParticipantDTO> participantDTOS = participants.stream().map(ParticipantMapper.INSTANCE::toDto).toList();

        participantDTOS.forEach(participantDTO -> {
            UUID userId = participantDTO.getUser().getId();
            UserDTO userDTO = userService.buildUserDTO(userId);
            participantDTO.setUser(userDTO);
        });

        return participantDTOS;
    }
}
