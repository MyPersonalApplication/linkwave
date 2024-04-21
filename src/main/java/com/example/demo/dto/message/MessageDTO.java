package com.example.demo.dto.message;

import com.example.demo.dto.conversation.ConversationDTO;
import com.example.demo.dto.user.UserDTO;
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
public class MessageDTO {
    private UUID id;
    private String content;
    private Boolean isRead;
    private ConversationDTO conversation;
    private UserDTO sender;
    private Date createdAt;
}
