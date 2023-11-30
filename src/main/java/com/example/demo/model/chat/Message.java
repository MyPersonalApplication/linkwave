package com.example.demo.model.chat;

import com.example.demo.model.BaseModel;
import com.example.demo.model.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;

import java.util.Set;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "messages")
@Where(clause = "archived = false")
public class Message extends BaseModel {
    @Id
    @GeneratedValue
    private UUID id;

    @Column
    private String content;

    @ManyToOne
    @JoinColumn(name = "conversation_id", nullable = false)
    private Conversation conversation;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @OneToMany(mappedBy = "message", cascade = CascadeType.ALL)
    private Set<Receipt> receipts;

    @OneToMany(mappedBy = "message", cascade = CascadeType.ALL)
    private Set<MessageAttachment> attachments;
}
