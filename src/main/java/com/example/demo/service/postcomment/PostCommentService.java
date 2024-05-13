package com.example.demo.service.postcomment;

import com.example.demo.dto.postcomment.CreatePostCommentDTO;
import com.example.demo.dto.postcomment.PostCommentDTO;
import com.example.demo.dto.replycomment.CreateReplyCommentDTO;
import com.example.demo.dto.replycomment.ReplyCommentDTO;

import java.util.List;
import java.util.UUID;

public interface PostCommentService {
    PostCommentDTO commentPost(UUID postId, CreatePostCommentDTO createPostCommentDTO);

    PostCommentDTO getPostComment(UUID postCommentId);

    ReplyCommentDTO replyCommentPost(UUID postCommentId, CreateReplyCommentDTO createReplyCommentDTO);

    ReplyCommentDTO getReplyComment(UUID replyCommentId);

    List<ReplyCommentDTO> getReplyComments(UUID postCommentId);

    void removeCommentPost(UUID postCommentId);

    List<PostCommentDTO> getPostComments(UUID postId);
}
