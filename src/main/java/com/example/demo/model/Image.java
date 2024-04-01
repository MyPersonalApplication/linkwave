package com.example.demo.model;

import com.example.demo.model.user.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@Setter
@MappedSuperclass
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Image extends BaseModel {
    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "image_id")
    private String imageId;
}
