package com.example.demo.model.album;

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
@Table(name = "albums")
@Where(clause = "archived = false")
public class Album extends BaseModel {
    @Id
    @GeneratedValue
    private UUID id;

    @Column
    private String name;

    @Column
    private String description;

    @Column(name = "is_public")
    private Boolean isPublic;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "album", cascade = CascadeType.ALL)
    private Set<AlbumMedia> albumMedia;
}
