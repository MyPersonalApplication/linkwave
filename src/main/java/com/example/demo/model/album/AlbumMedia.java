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

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "album_media")
@Where(clause = "archived = false")
public class AlbumMedia extends BaseModel {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String url;

    @Column
    private String caption;

    @Column(name = "is_video")
    private Boolean isVideo;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "album_id", nullable = false)
    private Album album;
}
