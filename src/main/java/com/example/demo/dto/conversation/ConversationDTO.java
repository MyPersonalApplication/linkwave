package com.example.demo.dto.conversation;

import com.example.demo.dto.message.MessageDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ConversationDTO {
    private UUID id;
    private String name;
    private List<MessageDTO> messages;
    private Date createdAt;
}
