package com.example.demo.dto.post;

import com.example.demo.dto.postmedia.PostMediaDTO;
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
public class PostDTO {
    private UUID id;
    private String content;
    private UserDTO user;
    private List<PostMediaDTO> lstMedia;
    private Date createdAt;
}
