package com.example.demo.dto.postcomment;

import com.example.demo.dto.likecomment.LikeCommentDTO;
import com.example.demo.dto.replycomment.ReplyCommentDTO;
import com.example.demo.dto.user.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class PostCommentDTO {
    private UUID id;
    private String content;
    private UUID postId;
    private UserDTO user;
    private List<ReplyCommentDTO> lstReplyComments;
    private List<LikeCommentDTO> lstLikeComments;
    private Date createdAt;
}
