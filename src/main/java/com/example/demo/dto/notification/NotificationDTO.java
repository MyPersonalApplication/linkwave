package com.example.demo.dto.notification;

import com.example.demo.dto.user.UserDTO;
import com.example.demo.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class NotificationDTO {
    private UUID id;
    private NotificationType notificationType;
    private String message;
    private Boolean isRead;
    private UserDTO sender;
    private UUID receiverId;
    private Date createdAt;
}
