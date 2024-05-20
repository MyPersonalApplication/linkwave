package com.example.demo.service.cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class CloudinaryServiceImpl implements CloudinaryService {
    private final Cloudinary cloudinary;

    @Override
    public Map uploadImage(MultipartFile multipartFile, String folderName) throws IOException {
        File file = convertMultiPartToFile(multipartFile);

        Map<String, Object> params = new HashMap<>();
        params.put("folder", folderName); // Specify the folder

        Map uploadResult = cloudinary.uploader().upload(file, params);

        if (!Files.deleteIfExists(file.toPath())) {
            log.error("Failed to delete file");
            throw new IOException("Failed to delete file: " + file.getAbsolutePath());
        }
        return uploadResult;
    }

    @Override
    public Map deleteImage(String id) throws IOException {
        return cloudinary.uploader().destroy(id, ObjectUtils.emptyMap());
    }

    @Override
    public Map uploadVideo(MultipartFile multipartFile, String folderName) throws IOException {
        File file = convertMultiPartToFile(multipartFile);

        Map<String, Object> params = new HashMap<>();
        params.put("folder", folderName); // Specify the folder
        params.put("resource_type", "video");

        Map uploadResult = cloudinary.uploader().upload(file, params);

        if (!Files.deleteIfExists(file.toPath())) {
            log.error("Failed to delete file");
            throw new IOException("Failed to delete file: " + file.getAbsolutePath());
        }
        return uploadResult;
    }

    @Override
    public File convertMultiPartToFile(MultipartFile multipartFile) throws IOException {
        File file = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(multipartFile.getBytes());
        fos.close();
        return file;
    }

    @Override
    public Map uploadFile(MultipartFile multipartFile, String folderName) throws IOException {
        File file = convertMultiPartToFile(multipartFile);

        Map<String, Object> params = new HashMap<>();
        params.put("folder", folderName); // Specify the folder

        // Determine the resource type based on the file's content type
        String resourceType;
        String contentType = multipartFile.getContentType();
        if (contentType != null && contentType.startsWith("image")) {
            resourceType = "image";
        } else if (contentType != null && contentType.startsWith("video")) {
            resourceType = "video";
        } else {
            resourceType = "raw"; // Default to raw for other file types
        }
        params.put("resource_type", resourceType);

        Map uploadResult = cloudinary.uploader().upload(file, params);

        if (!Files.deleteIfExists(file.toPath())) {
            log.error("Failed to delete file");
            throw new IOException("Failed to delete file: " + file.getAbsolutePath());
        }
        return uploadResult;
    }

}
