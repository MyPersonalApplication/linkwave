package com.example.demo.controller;

import com.example.demo.dto.post.CreatePostDTO;
import com.example.demo.dto.post.PostDTO;
import com.example.demo.dto.postlike.PostLikeDTO;
import com.example.demo.service.post.PostService;
import com.example.demo.service.postlike.PostLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/posts")
@PreAuthorize("hasRole('USER')")
@RequiredArgsConstructor
public class PostLikeController {
    private final PostLikeService postLikeService;

    @GetMapping("/{postId}/likes")
    public ResponseEntity<List<PostLikeDTO>> getPostLikes(@PathVariable UUID postId) {
        return ResponseEntity.ok(postLikeService.getPostLikes(postId));
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<PostLikeDTO> likePost(@PathVariable UUID postId) {
        return ResponseEntity.ok(postLikeService.likePost(postId));
    }

    @DeleteMapping("/{postLikeId}/unlike")
    public ResponseEntity<UUID> unlikePost(@PathVariable UUID postLikeId) {
        return ResponseEntity.ok(postLikeService.unlikePost(postLikeId));
    }
}
