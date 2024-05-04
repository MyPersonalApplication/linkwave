package com.example.demo.service.postmedia;

import com.example.demo.dto.postmedia.PostMediaDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface PostMediaService {
    List<PostMediaDTO> createPostMedia(List<MultipartFile> multipartFiles, UUID postId);
}
