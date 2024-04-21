package com.example.demo.repository;

import com.example.demo.model.chat.Conversation;
import com.example.demo.model.chat.Message;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface MessageRepository extends BaseRepository<Message>, JpaSpecificationExecutor<Message> {
    @Query("select m from Message m where m.conversation.id = ?1 order by m.createdAt asc")
    List<Message> findByConversationId(UUID conversationId);

    @Query("select m from Message m where m.id in ?1")
    List<Message> findByIds(List<UUID> listMessageIds);
}
