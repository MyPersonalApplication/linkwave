package com.example.demo.model.interact;

import com.example.demo.model.BaseModel;
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
@Table(name = "post_media")
@Where(clause = "archived = false")
public class PostMedia extends BaseModel {
    @Id
    @GeneratedValue
    private UUID id;

    @Column
    private String url;

    @Column
    private String caption;

    @Column(name = "is_video")
    private Boolean isVideo;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;
}
