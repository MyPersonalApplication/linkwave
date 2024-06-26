package com.example.demo.mapper;

import com.example.demo.dto.message.CreateMessageDTO;
import com.example.demo.dto.message.MessageDTO;
import com.example.demo.model.chat.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface MessageMapper {
    MessageMapper INSTANCE = Mappers.getMapper(MessageMapper.class);

    @Mapping(target = "conversationId", source = "conversation.id")
    @Mapping(target = "lstAttachments", source = "attachments")
    MessageDTO toDto(Message message);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "conversation.id", source = "conversationId")
    @Mapping(target = "sender.id", source = "senderId")
    Message toEntity(CreateMessageDTO createMessageDTO);
}
