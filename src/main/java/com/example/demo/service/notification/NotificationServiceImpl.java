package com.example.demo.service.notification;

import com.example.demo.config.authentication.TokenHandler;
import com.example.demo.controller.exception.NotFoundException;
import com.example.demo.dto.notification.CreateNotificationDTO;
import com.example.demo.dto.notification.NotificationDTO;
import com.example.demo.dto.user.UserDTO;
import com.example.demo.enums.ErrorMessage;
import com.example.demo.enums.NotificationMessage;
import com.example.demo.enums.NotificationType;
import com.example.demo.mapper.NotificationMapper;
import com.example.demo.model.Notification;
import com.example.demo.repository.NotificationRepository;
import com.example.demo.service.kafka.KafkaProducer;
import com.example.demo.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {
    private final TokenHandler tokenHandler;
    private final NotificationRepository notificationRepository;
    private final UserService userService;
    private final KafkaProducer kafkaProducer;

    @Override
    public NotificationDTO createNotification(CreateNotificationDTO createNotificationDTO) {
        // Get user id from token
        UUID senderId = tokenHandler.getUserId();

        if (createNotificationDTO.getSenderId() == null) {
            createNotificationDTO.setSenderId(senderId);
        }

        NotificationType notificationType = Enum.valueOf(NotificationType.class, createNotificationDTO.getNotificationType());

        switch (notificationType) {
            case FRIEND_REQUEST:
                createNotificationDTO.setMessage(NotificationMessage.FRIEND_REQUEST);
                break;
            case FRIEND_REQUEST_ACCEPTED:
                createNotificationDTO.setMessage(NotificationMessage.FRIEND_REQUEST_ACCEPTED);
                break;
            case NEW_POST:
                createNotificationDTO.setMessage(NotificationMessage.NEW_POST);
                break;
            case POST_LIKE:
                createNotificationDTO.setMessage(NotificationMessage.POST_LIKE);
                break;
            case POST_COMMENT:
                createNotificationDTO.setMessage(NotificationMessage.POST_COMMENT);
                break;
            case POST_COMMENT_LIKE:
                createNotificationDTO.setMessage(NotificationMessage.POST_COMMENT_LIKE);
                break;
            case POST_COMMENT_REPLY:
                createNotificationDTO.setMessage(NotificationMessage.POST_COMMENT_REPLY);
                break;
            default:
                throw new IllegalArgumentException(ErrorMessage.INVALID_NOTIFICATION_TYPE);
        }

        Notification notification = NotificationMapper.INSTANCE.toEntity(createNotificationDTO);
        notification.setCreatedAt(new Date());
        notification.setUpdatedAt(new Date());
        notificationRepository.save(notification);

        // Send message to kafka
        kafkaProducer.sendNotification(notification.getId().toString());

        return getNotification(notification.getId());
    }

    @Override
    public void markAsRead(UUID notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.NOTIFICATION_NOT_FOUND));
        notification.setIsRead(true);
        notificationRepository.save(notification);
    }

    @Override
    public void markAllAsRead() {
        // Get user id from token
        UUID receiverId = tokenHandler.getUserId();

        List<Notification> notifications = notificationRepository.findAllByReceiverId(receiverId);
        notifications.forEach(notification -> notification.setIsRead(true));
        notificationRepository.saveAll(notifications);
    }

    @Override
    public List<NotificationDTO> getNotifications() {
        // Get user id from token
        UUID receiverId = tokenHandler.getUserId();

        List<Notification> notifications = notificationRepository.findAllByReceiverId(receiverId);
        return notifications.stream()
                .map(notification -> {
                    NotificationDTO notificationDTO = NotificationMapper.INSTANCE.toDto(notification);
                    UserDTO userDTO = userService.buildUserDTO(notification.getSender().getId());
                    notificationDTO.setSender(userDTO);
                    return notificationDTO;
                })
                .toList();
    }

    @Override
    public NotificationDTO getNotification(UUID notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.NOTIFICATION_NOT_FOUND));
        NotificationDTO notificationDTO = NotificationMapper.INSTANCE.toDto(notification);
        UserDTO userDTO = userService.buildUserDTO(notification.getSender().getId());
        notificationDTO.setSender(userDTO);
        return notificationDTO;
    }

    @Override
    public void deleteNotification(UUID notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.NOTIFICATION_NOT_FOUND));
        notificationRepository.delete(notification);
    }
}
