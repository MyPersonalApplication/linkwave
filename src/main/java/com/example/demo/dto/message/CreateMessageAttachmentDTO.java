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
public class CreateMessageAttachmentDTO {
    private String fileUrl;
    private String fileId;
    private String fileName;
    private String fileType;
    private UUID messageId;
}
