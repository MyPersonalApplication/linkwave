package com.example.demo.model.interact;

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
@Table(name = "posts")
@Where(clause = "archived = false")
public class Post extends BaseModel {
    @Id
    @GeneratedValue
    private UUID id;

    @Column
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private Set<PostMedia> postMedia;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private Set<PostComment> postComments;
}
