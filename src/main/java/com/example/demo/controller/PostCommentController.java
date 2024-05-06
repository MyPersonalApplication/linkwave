package com.example.demo.controller;

import com.example.demo.dto.postcomment.CreatePostCommentDTO;
import com.example.demo.dto.postcomment.PostCommentDTO;
import com.example.demo.service.postcomment.PostCommentService;
import com.example.demo.service.postlike.PostLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/posts")
@PreAuthorize("hasRole('USER')")
@RequiredArgsConstructor
public class PostCommentController {
    private final PostCommentService postCommentService;

    @PostMapping("/{postId}/comment")
    public ResponseEntity<PostCommentDTO> commentPost(@PathVariable UUID postId, @RequestBody CreatePostCommentDTO createPostCommentDTO) {
        return ResponseEntity.ok(postCommentService.commentPost(postId, createPostCommentDTO));
    }

//    @DeleteMapping("/{postLikeId}/unlike")
//    public void unlikePost(@PathVariable UUID postLikeId) {
//        postLikeService.unlikePost(postLikeId);
//    }
}
