package com.example.demo.controller;

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
    public ResponseEntity<List<PostDTO>> getAll() {
        return ResponseEntity.ok(postService.getPosts());
    }
}
