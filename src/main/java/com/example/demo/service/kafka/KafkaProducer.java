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
    private static final String TOPIC_POST = "post";

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

    public void sendPost(String postId) {
        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(TOPIC_POST, postId);
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                System.out.println("Sent post=[" + postId +
                        "] with offset=[" + result.getRecordMetadata().offset() + "]");
            } else {
                System.out.println("Unable to send post=[" +
                        postId + "] due to : " + ex.getMessage());
            }
        });
    }
}
