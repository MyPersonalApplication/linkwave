package com.example.demo.service.postlike;

import com.example.demo.config.authentication.TokenHandler;
import com.example.demo.controller.exception.NotFoundException;
import com.example.demo.dto.postlike.CreatePostLikeDTO;
import com.example.demo.dto.postlike.PostLikeDTO;
import com.example.demo.dto.user.UserDTO;
import com.example.demo.enums.ErrorMessage;
import com.example.demo.mapper.PostLikeMapper;
import com.example.demo.mapper.PostMapper;
import com.example.demo.model.interact.Post;
import com.example.demo.model.interact.PostLike;
import com.example.demo.repository.PostLikeRepository;
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
    private final PostLikeRepository postLikeRepository;
    private final UserService userService;

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
}
