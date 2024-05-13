package com.example.demo.controller;

import com.example.demo.dto.postcomment.CreatePostCommentDTO;
import com.example.demo.dto.postcomment.PostCommentDTO;
import com.example.demo.dto.replycomment.CreateReplyCommentDTO;
import com.example.demo.dto.replycomment.ReplyCommentDTO;
import com.example.demo.service.postcomment.PostCommentService;
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
public class PostCommentController {
    private final PostCommentService postCommentService;

    @PostMapping("/{postId}/comment")
    public ResponseEntity<PostCommentDTO> commentPost(@PathVariable UUID postId, @RequestBody CreatePostCommentDTO createPostCommentDTO) {
        return ResponseEntity.ok(postCommentService.commentPost(postId, createPostCommentDTO));
    }

    @PostMapping("/comment/{postCommentId}/reply")
    public ResponseEntity<ReplyCommentDTO> replyCommentPost(@PathVariable UUID postCommentId, @RequestBody CreateReplyCommentDTO createReplyCommentDTO) {
        return ResponseEntity.ok(postCommentService.replyCommentPost(postCommentId, createReplyCommentDTO));
    }

    @GetMapping("/comment/{postCommentId}/reply")
    public ResponseEntity<List<ReplyCommentDTO>> getReplyCommentsPost(@PathVariable UUID postCommentId) {
        return ResponseEntity.ok(postCommentService.getReplyComments(postCommentId));
    }

    @GetMapping("/comment/{postCommentId}")
    public ResponseEntity<PostCommentDTO> getPostComment(@PathVariable UUID postCommentId) {
        return ResponseEntity.ok(postCommentService.getPostComment(postCommentId));
    }

    @GetMapping("/comment/reply/{replyCommentId}")
    public ResponseEntity<ReplyCommentDTO> getReplyCommentPost(@PathVariable UUID replyCommentId) {
        return ResponseEntity.ok(postCommentService.getReplyComment(replyCommentId));
    }
}
