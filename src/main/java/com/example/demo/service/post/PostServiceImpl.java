package com.example.demo.service.post;

import com.example.demo.config.authentication.TokenHandler;
import com.example.demo.controller.exception.NotFoundException;
import com.example.demo.dto.base.SearchResultDTO;
import com.example.demo.dto.likecomment.LikeCommentDTO;
import com.example.demo.dto.post.CreatePostDTO;
import com.example.demo.dto.post.PostDTO;
import com.example.demo.dto.postcomment.PostCommentDTO;
import com.example.demo.dto.replycomment.ReplyCommentDTO;
import com.example.demo.dto.user.UserDTO;
import com.example.demo.enums.ErrorMessage;
import com.example.demo.mapper.PostMapper;
import com.example.demo.model.interact.Post;
import com.example.demo.repository.PostRepository;
import com.example.demo.service.postcomment.PostCommentService;
import com.example.demo.service.postlike.PostLikeService;
import com.example.demo.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    private final PostCommentService postCommentService;
    private final PostLikeService postLikeService;

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
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.POST_NOT_FOUND));
        postRepository.delete(post);
    }

    @Override
    public void updatePost(PostDTO postDTO) {

    }

    @Override
    public PostDTO getPost(UUID postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new NotFoundException(ErrorMessage.POST_NOT_FOUND));
//        Hibernate.initialize(post.getPostMedia());
//        Hibernate.initialize(post.getPostComments());

        PostDTO postDTO = PostMapper.INSTANCE.toDto(post);
        UserDTO userDTO = userService.buildUserDTO(post.getUser().getId());
        postDTO.setUser(userDTO);

        List<PostCommentDTO> postCommentDTOS = postDTO.getLstComments().stream().map(comment -> {
            UserDTO userComment = userService.buildUserDTO(comment.getUser().getId());
            comment.setUser(userComment);

            List<ReplyCommentDTO> replyCommentDTOS = postCommentService.getReplyComments(comment.getId());
            comment.setLstReplyComments(replyCommentDTOS);

            List<LikeCommentDTO> likeCommentDTOS = postLikeService.getCommentLikes(comment.getId());
            comment.setLstLikeComments(likeCommentDTOS);

            return comment;
        }).toList();

        postDTO.setLstComments(postCommentDTOS);

        return postDTO;
    }

    @Override
    public SearchResultDTO getPosts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> postPageResult = postRepository.findAllPosts(pageable);
        return buildSearchResultDTOs(postPageResult, page, size);
    }

    @Override
    public SearchResultDTO searchPost(String query, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> postPageResult = postRepository.findByContentContainingIgnoreCase(query, pageable);
        return buildSearchResultDTOs(postPageResult, page, size);
    }

    private SearchResultDTO buildSearchResultDTOs(Page<Post> postPageResult, int page, int pageSize) {
        List<PostDTO> postDTOS = postPageResult.getContent().stream().map(post -> {
            PostDTO postDTO = PostMapper.INSTANCE.toDto(post);
            UserDTO userDTO = userService.buildUserDTO(post.getUser().getId());
            postDTO.setUser(userDTO);
            return postDTO;
        }).toList();

        SearchResultDTO searchResultDTO = new SearchResultDTO();
        searchResultDTO.setPage(page);
        searchResultDTO.setPageSize(pageSize);
        searchResultDTO.setTotalPages(postPageResult.getTotalPages());
        searchResultDTO.setContents(postDTOS);
        searchResultDTO.setTotalSize(postPageResult.getTotalElements());

        return searchResultDTO;
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
