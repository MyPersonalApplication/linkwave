package com.example.demo.mapper;

import com.example.demo.dto.postcomment.CreatePostCommentDTO;
import com.example.demo.dto.postcomment.PostCommentDTO;
import com.example.demo.dto.replycomment.CreateReplyCommentDTO;
import com.example.demo.dto.replycomment.ReplyCommentDTO;
import com.example.demo.model.interact.PostComment;
import com.example.demo.model.interact.ReplyComment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ReplyCommentMapper {
    ReplyCommentMapper INSTANCE = Mappers.getMapper(ReplyCommentMapper.class);

    @Mapping(target = "postCommentId", source = "postComment.id")
    ReplyCommentDTO toDto(ReplyComment replyComment);

    List<ReplyCommentDTO> toDto(List<ReplyComment> replyComments);

    @Mapping(target = "postComment.id", source = "postCommentId")
    @Mapping(target = "user.id", source = "userId")
    ReplyComment toEntity(CreateReplyCommentDTO createReplyCommentDTO);
}
