package com.example.demo.mapper;

import com.example.demo.dto.likecomment.CreateLikeCommentDTO;
import com.example.demo.dto.likecomment.LikeCommentDTO;
import com.example.demo.dto.postlike.CreatePostLikeDTO;
import com.example.demo.dto.postlike.PostLikeDTO;
import com.example.demo.model.interact.LikeComment;
import com.example.demo.model.interact.PostLike;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LikeCommentMapper {
    LikeCommentMapper INSTANCE = Mappers.getMapper(LikeCommentMapper.class);

    LikeCommentDTO toDto(LikeComment likeComment);

    @Mapping(target = "postComment.id", source = "postCommentId")
    @Mapping(target = "user.id", source = "userId")
    LikeComment toEntity(CreateLikeCommentDTO createLikeCommentDTO);
}
