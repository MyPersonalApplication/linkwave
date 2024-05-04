package com.example.demo.dto.postmedia;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class CreatePostMediaDTO {
    private String mediaUrl;
    private String mediaId;
    private Boolean isVideo;
    private UUID postId;
}
