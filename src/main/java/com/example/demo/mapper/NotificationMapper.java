package com.example.demo.mapper;

import com.example.demo.dto.notification.CreateNotificationDTO;
import com.example.demo.dto.notification.NotificationDTO;
import com.example.demo.dto.post.CreatePostDTO;
import com.example.demo.dto.post.PostDTO;
import com.example.demo.model.Notification;
import com.example.demo.model.interact.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface NotificationMapper {
    NotificationMapper INSTANCE = Mappers.getMapper(NotificationMapper.class);

    @Mapping(target = "receiverId", source = "receiver.id")
    NotificationDTO toDto(Notification notification);

    @Mapping(target = "sender.id", source = "senderId")
    @Mapping(target = "receiver.id", source = "receiverId")
    Notification toEntity(CreateNotificationDTO createNotificationDTO);
}
