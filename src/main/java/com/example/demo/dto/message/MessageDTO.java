package com.example.demo.dto.message;

import com.example.demo.dto.user.UserDTO;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString
public class MessageDTO {
    private UUID id;
    private String content;
    private Boolean isRead;
    private UUID conversationId;
    private UserDTO sender;
    private List<MessageAttachmentDTO> lstAttachments;
    private Date createdAt;
}
