package com.example.demo.mapper;

import com.example.demo.dto.message.CreateMessageAttachmentDTO;
import com.example.demo.dto.message.MessageAttachmentDTO;
import com.example.demo.dto.postmedia.CreatePostMediaDTO;
import com.example.demo.dto.postmedia.PostMediaDTO;
import com.example.demo.model.chat.MessageAttachment;
import com.example.demo.model.interact.PostMedia;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MessageAttachmentMapper {
    MessageAttachmentMapper INSTANCE = Mappers.getMapper(MessageAttachmentMapper.class);

    MessageAttachmentDTO toDto(MessageAttachment messageAttachment);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "message.id", source = "messageId")
    MessageAttachment toEntity(CreateMessageAttachmentDTO createMessageAttachmentDTO);
}
