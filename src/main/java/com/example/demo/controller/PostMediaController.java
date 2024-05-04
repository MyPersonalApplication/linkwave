package com.example.demo.controller;

import com.example.demo.dto.postmedia.PostMediaDTO;
import com.example.demo.service.postmedia.PostMediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/post-media")
@PreAuthorize("hasRole('USER')")
@RequiredArgsConstructor
public class PostMediaController {
    private final PostMediaService postMediaService;

    @PostMapping("/{postId}")
    public ResponseEntity<List<PostMediaDTO>> createPostMedia(@RequestPart("multipartFile") List<MultipartFile> multipartFiles, @PathVariable UUID postId) {
        return ResponseEntity.ok(postMediaService.createPostMedia(multipartFiles, postId));
    }
}
