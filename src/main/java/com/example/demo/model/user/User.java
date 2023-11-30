package com.example.demo.model.user;

import com.example.demo.model.album.AlbumMedia;
import com.example.demo.model.BaseModel;
import com.example.demo.model.Notification;
import com.example.demo.model.chat.Message;
import com.example.demo.model.chat.Participant;
import com.example.demo.model.chat.Receipt;
import com.example.demo.model.friend.FriendRequest;
import com.example.demo.model.friend.Friendship;
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
    @GeneratedValue
    private UUID id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "is_active")
    private Boolean isActive;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private List<Role> roles = new ArrayList<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private UserProfile userProfile;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<UserSkill> userSkills;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<UserExperience> userExperiences;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<UserActivity> userActivities;

    @OneToMany(mappedBy = "targetUser", cascade = CascadeType.ALL)
    private Set<UserActivity> targetUserActivities;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Notification> notifications;

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
    private Set<FriendRequest> sentFriendRequests;

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL)
    private Set<FriendRequest> receivedFriendRequests;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Friendship> friendships;

    @OneToMany(mappedBy = "friend", cascade = CascadeType.ALL)
    private Set<Friendship> friendOfFriendships;

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
    private Set<Message> messages;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Participant> participants;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Receipt> receipts;
}
