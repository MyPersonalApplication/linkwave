package com.example.demo.service.post;

import com.example.demo.dto.base.SearchResultDTO;
import com.example.demo.dto.post.CreatePostDTO;
import com.example.demo.dto.post.PostDTO;

import java.util.List;
import java.util.UUID;

public interface PostService {
    PostDTO createPost(CreatePostDTO createPostDTO);

    void deletePost(UUID postId);

    void updatePost(PostDTO postDTO);

    PostDTO getPost(UUID postId);

    SearchResultDTO getPosts(int page, int size);

    SearchResultDTO searchPost(int page, int size);

    List<PostDTO> getUserPosts(UUID userId);
}
