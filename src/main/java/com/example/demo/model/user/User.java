package com.example.demo.model.user;

import com.example.demo.model.album.Album;
import com.example.demo.model.album.AlbumMedia;
import com.example.demo.model.BaseModel;
import com.example.demo.model.Notification;
import com.example.demo.model.chat.Message;
import com.example.demo.model.chat.Participant;
import com.example.demo.model.chat.Receipt;
import com.example.demo.model.friend.FriendRequest;
import com.example.demo.model.friend.Friendship;
import com.example.demo.model.interact.LikeComment;
import com.example.demo.model.interact.Post;
import com.example.demo.model.interact.PostComment;
import com.example.demo.model.interact.ReplyComment;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;

import java.util.*;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
@Where(clause = "archived = false")
public class User extends BaseModel {
    @Id
    private UUID id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "is_active")
    private Boolean isActive;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private UserProfile userProfile;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<UserSkill> userSkills;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<UserExperience> userExperiences;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<UserActivity> userActivities;

    @OneToMany(mappedBy = "targetUser", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<UserActivity> targetUserActivities;

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Notification> sentNotifications;

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Notification> receivedNotifications;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Album> albums;

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<FriendRequest> sentFriendRequests;

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<FriendRequest> receivedFriendRequests;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Friendship> friendships;

    @OneToMany(mappedBy = "friend", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Friendship> friendOfFriendships;

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Message> messages;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Participant> participants;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Receipt> receipts;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Post> posts;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<PostComment> postComments;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<ReplyComment> replyComments;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<LikeComment> likeComments;
}
