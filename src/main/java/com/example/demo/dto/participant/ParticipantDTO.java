package com.example.demo.dto.participant;

import com.example.demo.dto.conversation.ConversationDTO;
import com.example.demo.dto.user.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ParticipantDTO {
    private UUID id;
    private UUID conversationId;
    private UserDTO user;
}
