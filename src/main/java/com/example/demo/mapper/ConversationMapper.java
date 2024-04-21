package com.example.demo.mapper;

import com.example.demo.dto.conversation.ConversationDTO;
import com.example.demo.model.chat.Conversation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ConversationMapper {
    ConversationMapper INSTANCE = Mappers.getMapper(ConversationMapper.class);

    @Mapping(target = "messages", ignore = true)
    ConversationDTO toDto(Conversation conversation);
}
