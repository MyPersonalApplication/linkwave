package com.example.demo.mapper;

import com.example.demo.dto.participant.CreateParticipantDTO;
import com.example.demo.dto.participant.ParticipantDTO;
import com.example.demo.model.chat.Participant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ParticipantMapper {
    ParticipantMapper INSTANCE = Mappers.getMapper(ParticipantMapper.class);

    Participant toEntity(ParticipantDTO participantDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "conversation.id", source = "conversationId")
    @Mapping(target = "user.id", source = "userId")
    Participant mapFromCreate(CreateParticipantDTO createParticipantDTO);

    @Mapping(target = "conversationId", source = "conversation.id")
    ParticipantDTO toDto(Participant participant);
}
