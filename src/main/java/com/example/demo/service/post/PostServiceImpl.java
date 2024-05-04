package com.example.demo.service.post;

import com.example.demo.config.authentication.TokenHandler;
import com.example.demo.dto.post.CreatePostDTO;
import com.example.demo.dto.post.PostDTO;
import com.example.demo.dto.user.UserDTO;
import com.example.demo.mapper.PostMapper;
import com.example.demo.model.interact.Post;
import com.example.demo.repository.PostRepository;
import com.example.demo.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
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
        return null;
    }

    @Override
    public List<PostDTO> getPosts() {
        List<Post> postList = postRepository.findAll();

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
