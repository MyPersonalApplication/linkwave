package com.example.demo.service.kafka;

import com.example.demo.dto.message.MessageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class KafkaProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private static final String TOPIC_CHAT_MESSAGE = "chat-message";
    private static final String TOPIC_POST_COMMENT = "post-comment";
    private static final String TOPIC_REPLY_COMMENT = "reply-comment";
    private static final String TOPIC_NOTIFICATION = "notification";

    public void sendMessage(String messageId) {
        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(TOPIC_CHAT_MESSAGE, messageId);
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                System.out.println("Sent message=[" + messageId +
                        "] with offset=[" + result.getRecordMetadata().offset() + "]");
            } else {
                System.out.println("Unable to send message=[" +
                        messageId + "] due to : " + ex.getMessage());
            }
        });
    }

    public void sendPostComment(String postCommentId) {
        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(TOPIC_POST_COMMENT, postCommentId);
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                System.out.println("Sent message=[" + postCommentId +
                        "] with offset=[" + result.getRecordMetadata().offset() + "]");
            } else {
                System.out.println("Unable to send message=[" +
                        postCommentId + "] due to : " + ex.getMessage());
            }
        });
    }

    public void sendReplyComment(String replyCommentId) {
        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(TOPIC_REPLY_COMMENT, replyCommentId);
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                System.out.println("Sent message=[" + replyCommentId +
                        "] with offset=[" + result.getRecordMetadata().offset() + "]");
            } else {
                System.out.println("Unable to send message=[" +
                        replyCommentId + "] due to : " + ex.getMessage());
            }
        });
    }

    public void sendNotification(String notificationId) {
        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(TOPIC_NOTIFICATION, notificationId);
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                System.out.println("Sent message=[" + notificationId +
                        "] with offset=[" + result.getRecordMetadata().offset() + "]");
            } else {
                System.out.println("Unable to send message=[" +
                        notificationId + "] due to : " + ex.getMessage());
            }
        });
    }
}
