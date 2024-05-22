package com.example.demo.repository;

import com.example.demo.model.chat.MessageAttachment;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageAttachmentRepository extends BaseRepository<MessageAttachment>, JpaSpecificationExecutor<MessageAttachment> {
}
