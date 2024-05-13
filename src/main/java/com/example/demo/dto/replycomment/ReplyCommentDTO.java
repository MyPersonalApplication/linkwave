package com.example.demo.dto.replycomment;

import com.example.demo.dto.postcomment.PostCommentDTO;
import com.example.demo.dto.user.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ReplyCommentDTO {
    private UUID id;
    private String content;
    private UUID postCommentId;
    private UserDTO user;
    private Date createdAt;
}
