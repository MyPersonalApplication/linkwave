package com.example.demo.repository;

import com.example.demo.model.chat.Conversation;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ConversationRepository extends BaseRepository<Conversation>, JpaSpecificationExecutor<Conversation> {
    @Query("""
                select c
                from Conversation c
                join c.participants p
                where p.user.id != :userId
                and c.id in (
                    select p.conversation.id
                    from Participant p
                    where p.user.id = :userId
                )
            """)
    List<Conversation> findByUserId(UUID userId);
}
