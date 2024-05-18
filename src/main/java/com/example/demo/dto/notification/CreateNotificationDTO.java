package com.example.demo.dto.notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CreateNotificationDTO {
    private String notificationType;
    private String message;
    private Boolean isRead = false;
    private UUID senderId;
    private UUID receiverId;
}
