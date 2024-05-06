package com.example.demo.service.postcomment;

import com.example.demo.config.authentication.TokenHandler;
import com.example.demo.controller.exception.NotFoundException;
import com.example.demo.dto.postcomment.CreatePostCommentDTO;
import com.example.demo.dto.postcomment.PostCommentDTO;
import com.example.demo.dto.user.UserDTO;
import com.example.demo.enums.ErrorMessage;
import com.example.demo.mapper.PostCommentMapper;
import com.example.demo.model.interact.PostComment;
import com.example.demo.repository.PostCommentRepository;
import com.example.demo.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostCommentServiceImpl implements PostCommentService {
    private final TokenHandler tokenHandler;
    private final PostCommentRepository postCommentRepository;
    private final UserService userService;

    @Override
    public PostCommentDTO commentPost(UUID postId, CreatePostCommentDTO createPostCommentDTO) {
        // Get user id from token
        UUID userId = tokenHandler.getUserId();

        createPostCommentDTO.setPostId(postId);
        createPostCommentDTO.setUserId(userId);

        PostComment postComment = PostCommentMapper.INSTANCE.toEntity(createPostCommentDTO);

        if (postComment.getPost() == null) {
            throw new NotFoundException(ErrorMessage.POST_NOT_FOUND);
        }

        postComment.setCreatedAt(new Date());
        postComment.setUpdatedAt(new Date());

        postCommentRepository.save(postComment);

        // Convert to DTO
        PostCommentDTO postCommentDTO = PostCommentMapper.INSTANCE.toDto(postComment);
        UserDTO userDTO = userService.buildUserDTO(postCommentDTO.getUser().getId());
        postCommentDTO.setUser(userDTO);

        return postCommentDTO;
    }

    @Override
    public void removeCommentPost(UUID postCommentId) {

    }

    @Override
    public List<PostCommentDTO> getPostComments(UUID postId) {
        return List.of();
    }
}
