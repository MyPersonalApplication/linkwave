package com.example.demo.repository;

import com.example.demo.model.chat.Message;
import com.example.demo.model.chat.MessageAttachment;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface MessageAttachmentRepository extends BaseRepository<MessageAttachment>, JpaSpecificationExecutor<MessageAttachment> {
}
