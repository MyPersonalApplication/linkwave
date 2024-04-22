package com.example.demo.service.participant;

import com.example.demo.dto.participant.ParticipantDTO;

import java.util.List;
import java.util.UUID;

public interface ParticipantService {
    List<ParticipantDTO> getParticipants(UUID conversationId);
}
