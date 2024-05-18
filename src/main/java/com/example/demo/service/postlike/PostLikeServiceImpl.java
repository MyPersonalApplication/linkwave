package com.example.demo.service.postlike;

import com.example.demo.config.authentication.TokenHandler;
import com.example.demo.controller.exception.NotFoundException;
import com.example.demo.dto.likecomment.CreateLikeCommentDTO;
import com.example.demo.dto.likecomment.LikeCommentDTO;
import com.example.demo.dto.notification.CreateNotificationDTO;
import com.example.demo.dto.postlike.CreatePostLikeDTO;
import com.example.demo.dto.postlike.PostLikeDTO;
import com.example.demo.dto.user.UserDTO;
import com.example.demo.enums.ErrorMessage;
import com.example.demo.enums.NotificationType;
import com.example.demo.mapper.LikeCommentMapper;
import com.example.demo.mapper.PostLikeMapper;
import com.example.demo.mapper.PostMapper;
import com.example.demo.model.interact.LikeComment;
import com.example.demo.model.interact.Post;
import com.example.demo.model.interact.PostComment;
import com.example.demo.model.interact.PostLike;
import com.example.demo.repository.LikeCommentRepository;
import com.example.demo.repository.PostCommentRepository;
import com.example.demo.repository.PostLikeRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.service.notification.NotificationService;
import com.example.demo.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostLikeServiceImpl implements PostLikeService {
    private final TokenHandler tokenHandler;
    private final PostRepository postRepository;
    private final PostCommentRepository postCommentRepository;
    private final PostLikeRepository postLikeRepository;
    private final LikeCommentRepository likeCommentRepository;
    private final UserService userService;
    private final NotificationService notificationService;

    @Override
    public PostLikeDTO likePost(UUID postId) {
        // Get user id from token
        UUID userId = tokenHandler.getUserId();

        CreatePostLikeDTO createPostLikeDTO = CreatePostLikeDTO.builder()
                .postId(postId)
                .userId(userId)
                .build();

        PostLike postLike = PostLikeMapper.INSTANCE.toEntity(createPostLikeDTO);

        if (postLike.getPost() == null) {
            throw new NotFoundException(ErrorMessage.POST_NOT_FOUND);
        }

        postLike.setCreatedAt(new Date());
        postLike.setUpdatedAt(new Date());

        postLikeRepository.save(postLike);

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.POST_NOT_FOUND));
        CreateNotificationDTO createNotificationDTO = CreateNotificationDTO.builder()
                .notificationType(String.valueOf(NotificationType.POST_LIKE))
                .isRead(false)
                .senderId(userId)
                .receiverId(post.getUser().getId())
                .build();
        notificationService.createNotification(createNotificationDTO);

        return PostLikeMapper.INSTANCE.toDto(postLike);
    }

    @Override
    public UUID unlikePost(UUID postLikeId) {
        PostLike postLike = postLikeRepository.findById(postLikeId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.POST_LIKE_NOT_FOUND));

        postLikeRepository.delete(postLike);

        return postLike.getId();
    }

    @Override
    public List<PostLikeDTO> getPostLikes(UUID postId) {
        List<PostLike> postLikes = postLikeRepository.findAllByPostId(postId);

        return postLikes.stream()
                .map(postLike -> {
                    PostLikeDTO postLikeDTO = PostLikeMapper.INSTANCE.toDto(postLike);
                    UserDTO userDTO = userService.buildUserDTO(postLike.getUser().getId());
                    postLikeDTO.setUser(userDTO);
                    return postLikeDTO;
                })
                .toList();
    }

    @Override
    public LikeCommentDTO likeComment(UUID postCommentId) {
        // Get user id from token
        UUID userId = tokenHandler.getUserId();

        CreateLikeCommentDTO createLikeCommentDTO = CreateLikeCommentDTO.builder()
                .postCommentId(postCommentId)
                .userId(userId)
                .build();

        LikeComment likeComment = LikeCommentMapper.INSTANCE.toEntity(createLikeCommentDTO);

        if (likeComment.getPostComment() == null) {
            throw new NotFoundException(ErrorMessage.POST_COMMENT_NOT_FOUND);
        }

        likeComment.setCreatedAt(new Date());
        likeComment.setUpdatedAt(new Date());

        likeCommentRepository.save(likeComment);

        PostComment postComment = postCommentRepository.findById(postCommentId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.POST_COMMENT_NOT_FOUND));
        CreateNotificationDTO createNotificationDTO = CreateNotificationDTO.builder()
                .notificationType(String.valueOf(NotificationType.POST_COMMENT_LIKE))
                .isRead(false)
                .senderId(userId)
                .receiverId(postComment.getUser().getId())
                .build();
        notificationService.createNotification(createNotificationDTO);

        return LikeCommentMapper.INSTANCE.toDto(likeComment);
    }

    @Override
    public UUID unlikeComment(UUID likeCommentId) {
        LikeComment likeComment = likeCommentRepository.findById(likeCommentId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.LIKE_COMMENT_NOT_FOUND));

        likeCommentRepository.delete(likeComment);

        return likeComment.getId();
    }

    @Override
    public List<LikeCommentDTO> getCommentLikes(UUID postCommentId) {
        List<LikeComment> likeComments = likeCommentRepository.findAllByPostCommentId(postCommentId);

        return likeComments.stream()
                .map(likeComment -> {
                    LikeCommentDTO likeCommentDTO = LikeCommentMapper.INSTANCE.toDto(likeComment);
                    UserDTO userDTO = userService.buildUserDTO(likeComment.getUser().getId());
                    likeCommentDTO.setUser(userDTO);
                    return likeCommentDTO;
                })
                .toList();
    }
}
