package com.example.demo.mapper;

import com.example.demo.dto.postcomment.CreatePostCommentDTO;
import com.example.demo.dto.postcomment.PostCommentDTO;
import com.example.demo.model.interact.PostComment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PostCommentMapper {
    PostCommentMapper INSTANCE = Mappers.getMapper(PostCommentMapper.class);

    @Mapping(target = "postId", source = "post.id")
    @Mapping(target = "lstReplyComments", source = "replyComments")
    @Mapping(target = "lstLikeComments", source = "likeComments")
    PostCommentDTO toDto(PostComment postComment);

    @Mapping(target = "post.id", source = "postId")
    @Mapping(target = "user.id", source = "userId")
    PostComment toEntity(CreatePostCommentDTO createPostCommentDTO);
}
