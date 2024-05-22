package com.example.demo.repository;

import com.example.demo.model.chat.Participant;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ParticipantRepository extends BaseRepository<Participant>, JpaSpecificationExecutor<Participant> {
    @Query("""
                select p
                from Participant p
                where p.user.id = :friend
                and p.conversation.id in (
                    select p.conversation.id
                    from Participant p
                    where p.user.id = :userId
                )
            """)
    Participant isExistConversation(UUID friend, UUID userId);

    @Query("select m from Participant m where m.conversation.id = ?1")
    List<Participant> findByConversationId(UUID conversationId);

    @Query("select p from Participant p where p.user.id = ?1 order by p.conversation.updatedAt desc")
    List<Participant> findByUserId(UUID userId);
}
