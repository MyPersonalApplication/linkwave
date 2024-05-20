package com.example.demo.dto.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class MessageAttachmentDTO {
    private UUID id;
    private String fileUrl;
    private String fileId;
    private String fileName;
    private String fileType;
}
