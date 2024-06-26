package com.example.demo.service.kafka;

import com.example.demo.dto.message.MessageDTO;
import com.example.demo.dto.notification.NotificationDTO;
import com.example.demo.service.message.MessageService;
import com.example.demo.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class KafkaListeners {
    private final MessageService messageService;
    private final NotificationService notificationService;
    private final SimpMessagingTemplate messagingTemplate;

    @KafkaListener(
            topics = "chat-message",
            groupId = "linkwave-group"
    )
    public void listenMessage(UUID messageId) {
        System.out.println("Received message: " + messageId);

        MessageDTO message = messageService.getMessage(messageId);
        messagingTemplate.convertAndSend("/topic/chat", message);
    }

    @KafkaListener(
            topics = "post-comment",
            groupId = "linkwave-group"
    )
    public void listenPostComment(UUID postCommentId) {
        System.out.println("Received post comment: " + postCommentId);

//        PostCommentDTO postCommentDTO = postCommentService.getPostComment(postCommentId);
        messagingTemplate.convertAndSend("/topic/post-comment", postCommentId);
    }

    @KafkaListener(
            topics = "reply-comment",
            groupId = "linkwave-group"
    )
    public void listenReplyComment(UUID replyCommentId) {
        System.out.println("Received reply comment: " + replyCommentId);

//        ReplyCommentDTO replyCommentDTO = postCommentService.getReplyComment(replyCommentId);
        messagingTemplate.convertAndSend("/topic/reply-comment", replyCommentId);
    }

    @KafkaListener(
            topics = "notification",
            groupId = "linkwave-group"
    )
    public void listenNotification(UUID notificationId) {
        System.out.println("Received notification: " + notificationId);

        NotificationDTO notificationDTO = notificationService.getNotification(notificationId);
        messagingTemplate.convertAndSend("/topic/notification", notificationDTO);
    }
}
