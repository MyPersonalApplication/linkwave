package com.example.demo.controller;

import com.example.demo.dto.base.SearchResultDTO;
import com.example.demo.dto.message.CreateMessageDTO;
import com.example.demo.dto.message.MessageDTO;
import com.example.demo.dto.post.CreatePostDTO;
import com.example.demo.dto.post.PostDTO;
import com.example.demo.service.post.PostService;
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
public class PostController {
    private final PostService postService;

    @PostMapping()
    public ResponseEntity<PostDTO> createPost(@RequestBody CreatePostDTO createPostDTO) {
        return ResponseEntity.ok(postService.createPost(createPostDTO));
    }

    @GetMapping()
    public ResponseEntity<SearchResultDTO> getAll(@RequestParam(required = false, defaultValue = "0") int page,
                                                  @RequestParam(required = false, defaultValue = "10") int pageSize) {
        return ResponseEntity.ok(postService.getPosts(page, pageSize));
    }

    @GetMapping("/search")
    public ResponseEntity<SearchResultDTO> search(@RequestParam String query,
                                                  @RequestParam(required = false, defaultValue = "0") int page,
                                                  @RequestParam(required = false, defaultValue = "10") int pageSize) {
        return ResponseEntity.ok(postService.searchPost(query, page, pageSize));
    }

    @GetMapping("user/{userId}")
    public ResponseEntity<List<PostDTO>> getUserPosts(@PathVariable UUID userId) {
        return ResponseEntity.ok(postService.getUserPosts(userId));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDTO> getPost(@PathVariable UUID postId) {
        return ResponseEntity.ok(postService.getPost(postId));
    }
}
