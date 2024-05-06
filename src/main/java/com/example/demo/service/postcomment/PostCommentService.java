package com.example.demo.service.postcomment;

import com.example.demo.dto.postcomment.CreatePostCommentDTO;
import com.example.demo.dto.postcomment.PostCommentDTO;

import java.util.List;
import java.util.UUID;

public interface PostCommentService {
    PostCommentDTO commentPost(UUID postId, CreatePostCommentDTO createPostCommentDTO);
    void removeCommentPost(UUID postCommentId);
    List<PostCommentDTO> getPostComments(UUID postId);
}
