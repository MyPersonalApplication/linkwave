package com.example.demo.repository;

import com.example.demo.model.chat.Conversation;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public interface ConversationRepository extends BaseRepository<Conversation>, JpaSpecificationExecutor<Conversation> {
//    @Query("""
//                select temp1, temp2
//                from (
//                    select c
//                    from Conversation c
//                    join c.participants p
//                    join p.user u
//                    where u.id != :userId
//                    and c.id in (
//                        select p.conversation.id
//                        from Participant p
//                        where p.user.id = :userId
//                    )
//                ) temp1
//                join (
//                    select distinct c
//                    from Conversation c
//                    join c.messages m
//                    join c.participants p
//                    join m.receipts r
//                    where p.user.id = :userId
//                    order by c.id, m.createdAt desc
//                ) temp2
//                order by temp2.createdAt desc
//            """)
//    List<Objects> findAllByUserId(UUID userId);

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
