package com.example.demo.model.chat;

import com.example.demo.model.BaseModel;
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
@Table(name = "conversations")
@Where(clause = "archived = false")
public class Conversation extends BaseModel {
    @Id
    @GeneratedValue
    private UUID id;

    @Column
    private String name;

    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL)
    private Set <Message> messages;

    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL)
    private Set <Participant> participants;
}
