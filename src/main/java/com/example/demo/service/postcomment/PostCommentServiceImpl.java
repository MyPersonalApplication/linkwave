package com.example.demo.service.postcomment;

import com.example.demo.config.authentication.TokenHandler;
import com.example.demo.controller.exception.NotFoundException;
import com.example.demo.dto.postcomment.CreatePostCommentDTO;
import com.example.demo.dto.postcomment.PostCommentDTO;
import com.example.demo.dto.replycomment.CreateReplyCommentDTO;
import com.example.demo.dto.replycomment.ReplyCommentDTO;
import com.example.demo.dto.user.UserDTO;
import com.example.demo.enums.ErrorMessage;
import com.example.demo.mapper.PostCommentMapper;
import com.example.demo.mapper.ReplyCommentMapper;
import com.example.demo.model.interact.PostComment;
import com.example.demo.model.interact.ReplyComment;
import com.example.demo.repository.PostCommentRepository;
import com.example.demo.repository.ReplyCommentRepository;
import com.example.demo.service.kafka.KafkaProducer;
import com.example.demo.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostCommentServiceImpl implements PostCommentService {
    private final TokenHandler tokenHandler;
    private final PostCommentRepository postCommentRepository;
    private final ReplyCommentRepository replyCommentRepository;
    private final UserService userService;
    private final KafkaProducer kafkaProducer;

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
        postCommentDTO.setLstReplyComments(List.of());

        // Send message to kafka
        kafkaProducer.sendPostComment(String.valueOf(postCommentDTO.getId()));

        return postCommentDTO;
    }

    @Override
    public PostCommentDTO getPostComment(UUID postCommentId) {
        PostComment postComment = postCommentRepository.findById(postCommentId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.POST_COMMENT_NOT_FOUND));
        Hibernate.initialize(postComment.getReplyComments());

        PostCommentDTO postCommentDTO = PostCommentMapper.INSTANCE.toDto(postComment);
        UserDTO userDTO = userService.buildUserDTO(postCommentDTO.getUser().getId());
        postCommentDTO.setUser(userDTO);

        return postCommentDTO;
    }

    @Override
    public ReplyCommentDTO replyCommentPost(UUID postCommentId, CreateReplyCommentDTO createReplyCommentDTO) {
        // Get user id from token
        UUID userId = tokenHandler.getUserId();

        createReplyCommentDTO.setPostCommentId(postCommentId);
        createReplyCommentDTO.setUserId(userId);

        ReplyComment replyComment = ReplyCommentMapper.INSTANCE.toEntity(createReplyCommentDTO);

        if (replyComment.getPostComment() == null) {
            throw new NotFoundException(ErrorMessage.POST_NOT_FOUND);
        }

        replyComment.setCreatedAt(new Date());
        replyComment.setUpdatedAt(new Date());

        replyCommentRepository.save(replyComment);

        // Convert to DTO
        ReplyCommentDTO replyCommentDTO = ReplyCommentMapper.INSTANCE.toDto(replyComment);
        UserDTO userDTO = userService.buildUserDTO(replyCommentDTO.getUser().getId());
        replyCommentDTO.setUser(userDTO);

        // Send message to kafka
        kafkaProducer.sendReplyComment(String.valueOf(replyComment.getId()));

        return replyCommentDTO;
    }

    @Override
    public ReplyCommentDTO getReplyComment(UUID replyCommentId) {
        ReplyComment replyComment = replyCommentRepository.findById(replyCommentId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.REPLY_COMMENT_NOT_FOUND));

        ReplyCommentDTO replyCommentDTO = ReplyCommentMapper.INSTANCE.toDto(replyComment);
        UserDTO userDTO = userService.buildUserDTO(replyCommentDTO.getUser().getId());
        replyCommentDTO.setUser(userDTO);

        return replyCommentDTO;
    }

    @Override
    public List<ReplyCommentDTO> getReplyComments(UUID postCommentId) {
        List<ReplyComment> replyComments = replyCommentRepository.findByPostCommentId(postCommentId);

        List<ReplyCommentDTO> replyCommentDTOS = ReplyCommentMapper.INSTANCE.toDto(replyComments);

        replyCommentDTOS.forEach(replyCommentDTO -> {
            UserDTO userDTO = userService.buildUserDTO(replyCommentDTO.getUser().getId());
            replyCommentDTO.setUser(userDTO);
        });

        return replyCommentDTOS;
    }

    @Override
    public void removeCommentPost(UUID postCommentId) {

    }

    @Override
    public List<PostCommentDTO> getPostComments(UUID postId) {
        return List.of();
    }
}
