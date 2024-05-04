package com.example.demo.service.cloudinary;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public interface CloudinaryService {
    Map uploadImage(MultipartFile multipartFile, String folderName) throws IOException;
    Map deleteImage(String id) throws IOException;
    Map uploadVideo(MultipartFile multipartFile, String folderName) throws IOException;
    File convertMultiPartToFile(MultipartFile multipartFile) throws IOException;
}
