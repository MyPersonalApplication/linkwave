package com.example.demo.service.postmedia;

import com.example.demo.controller.exception.InvalidDataException;
import com.example.demo.dto.postmedia.CreatePostMediaDTO;
import com.example.demo.dto.postmedia.PostMediaDTO;
import com.example.demo.enums.ErrorMessage;
import com.example.demo.mapper.PostMediaMapper;
import com.example.demo.model.interact.PostMedia;
import com.example.demo.repository.PostMediaRepository;
import com.example.demo.service.cloudinary.CloudinaryService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostMediaServiceImpl implements PostMediaService {
    private final CloudinaryService cloudinaryService;
    private final Environment environment;
    private final PostMediaRepository postMediaRepository;

    @SneakyThrows
    @Override
    public List<PostMediaDTO> createPostMedia(List<MultipartFile> multipartFiles, UUID postId) {
        List<PostMediaDTO> postMediaDTOS = multipartFiles.stream()
                .map(multipartFile -> {
                    try {
                        return processSaveImage(multipartFile, postId);
                    } catch (IOException e) {
                        throw new InvalidDataException(ErrorMessage.FAILED_TO_SAVE_IMAGE);
                    }
                })
                .toList();

        return postMediaDTOS;
    }

    private PostMediaDTO processSaveImage(MultipartFile multipartFile, UUID postId) throws IOException {
        String[] activeProfiles = environment.getActiveProfiles();
        Map result;

        // Check if the file is an image
        BufferedImage bi = ImageIO.read(multipartFile.getInputStream());
        if (bi == null) {
            result = cloudinaryService.uploadVideo(multipartFile, (activeProfiles.length == 0 ? "prod" : activeProfiles[0]) + "/post-media/video");
        } else {
            result = cloudinaryService.uploadImage(multipartFile, (activeProfiles.length == 0 ? "prod" : activeProfiles[0]) + "/post-media/image");
        }

        CreatePostMediaDTO createPostMediaDTO = CreatePostMediaDTO.builder()
                .mediaUrl((String) result.get("url"))
                .mediaId((String) result.get("public_id"))
                .isVideo(result.get("resource_type").equals("video"))
                .postId(postId)
                .build();

        PostMedia postMedia = PostMediaMapper.INSTANCE.toEntity(createPostMediaDTO);
        postMedia.setCreatedAt(new Date());
        postMedia.setUpdatedAt(new Date());
        postMediaRepository.save(postMedia);

        return PostMediaMapper.INSTANCE.toDto(postMedia);
    }
}
