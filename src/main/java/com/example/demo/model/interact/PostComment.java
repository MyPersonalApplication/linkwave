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
@Table(name = "post_comments")
@Where(clause = "archived = false")
public class PostComment extends BaseModel {
    @Id
    @GeneratedValue
    private UUID id;

    @Column
    private String content;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "postComment", cascade = CascadeType.ALL)
    private Set<ReplyComment> replyComments;

    @OneToMany(mappedBy = "postComment", cascade = CascadeType.ALL)
    private Set<LikeComment> likeComments;
}
