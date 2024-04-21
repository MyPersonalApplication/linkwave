package com.example.demo.repository;

import com.example.demo.model.chat.Participant;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

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

    @Query("select p from Participant p where p.user.id = ?1")
    List<Participant> findByUserId(UUID userId);
}
