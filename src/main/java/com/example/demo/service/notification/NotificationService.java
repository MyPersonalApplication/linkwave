package com.example.demo.service.notification;

import com.example.demo.dto.notification.CreateNotificationDTO;
import com.example.demo.dto.notification.NotificationDTO;

import java.util.List;
import java.util.UUID;

public interface NotificationService {
    NotificationDTO createNotification(CreateNotificationDTO createNotificationDTO);

    void markAsRead(UUID notificationId);

    void markAllAsRead();

    List<NotificationDTO> getNotifications();

    NotificationDTO getNotification(UUID notificationId);

    void deleteNotification(UUID notificationId);
}
