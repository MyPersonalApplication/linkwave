package com.example.demo.service.post;

import com.example.demo.config.authentication.TokenHandler;
import com.example.demo.controller.exception.NotFoundException;
import com.example.demo.dto.post.CreatePostDTO;
import com.example.demo.dto.post.PostDTO;
import com.example.demo.dto.postcomment.PostCommentDTO;
import com.example.demo.dto.user.UserDTO;
import com.example.demo.enums.ErrorMessage;
import com.example.demo.mapper.PostMapper;
import com.example.demo.model.interact.Post;
import com.example.demo.repository.PostRepository;
import com.example.demo.service.user.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PostServiceImpl implements PostService {
    private final TokenHandler tokenHandler;
    private final PostRepository postRepository;
    private final UserService userService;

    @Override
    public PostDTO createPost(CreatePostDTO createPostDTO) {
        // Get user id from token
        UUID userId = tokenHandler.getUserId();

        if (createPostDTO.getUserId() == null) {
            createPostDTO.setUserId(userId);
        }

        // Create post
        Post post = PostMapper.INSTANCE.toEntity(createPostDTO);
        post.setCreatedAt(new Date());
        post.setUpdatedAt(new Date());
        postRepository.save(post);

        // Map post to postDTO
        PostDTO postDTO = PostMapper.INSTANCE.toDto(post);

        UserDTO userDTO = userService.buildUserDTO(post.getUser().getId());
        postDTO.setUser(userDTO);

        return postDTO;
    }

    @Override
    public void deletePost(UUID postId) {

    }

    @Override
    public void updatePost(PostDTO postDTO) {

    }

    @Override
    public PostDTO getPost(UUID postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new NotFoundException(ErrorMessage.POST_NOT_FOUND));
        Hibernate.initialize(post.getPostMedia());

        PostDTO postDTO = PostMapper.INSTANCE.toDto(post);
        UserDTO userDTO = userService.buildUserDTO(post.getUser().getId());
        postDTO.setUser(userDTO);

        List<PostCommentDTO> postCommentDTOS = postDTO.getLstComments().stream().map(comment -> {
            UserDTO userComment = userService.buildUserDTO(comment.getUser().getId());
            comment.setUser(userComment);
            return comment;
        }).toList();

        postDTO.setLstComments(postCommentDTOS);

        return postDTO;
    }

    @Override
    public List<PostDTO> getPosts() {
        List<Post> postList = postRepository.findAllPosts();

        return postList.stream()
                .map(post -> {
                    PostDTO postDTO = PostMapper.INSTANCE.toDto(post);
                    UserDTO userDTO = userService.buildUserDTO(post.getUser().getId());
                    postDTO.setUser(userDTO);
                    return postDTO;
                })
                .toList();
    }

    @Override
    public List<PostDTO> getUserPosts(UUID userId) {
        List<Post> postList = postRepository.findAllByUserId(userId);

        return postList.stream()
                .map(post -> {
                    PostDTO postDTO = PostMapper.INSTANCE.toDto(post);
                    UserDTO userDTO = userService.buildUserDTO(post.getUser().getId());
                    postDTO.setUser(userDTO);
                    return postDTO;
                })
                .toList();
    }
}
