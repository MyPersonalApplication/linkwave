package com.example.demo.repository;

import com.example.demo.model.friend.FriendRequest;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FriendRequestRepository extends BaseRepository<FriendRequest>, JpaSpecificationExecutor<FriendRequest> {
    @Query("""
            select fr
                from FriendRequest fr
                where fr.receiver.id = :receiverId
            """)
    List<FriendRequest> findByReceiverId(UUID receiverId);
    List<FriendRequest> findBySenderId(UUID senderId);
    FriendRequest findBySenderIdAndReceiverId(UUID senderId, UUID receiverId);
    void deleteBySenderIdAndReceiverId(UUID senderId, UUID receiverId);
    void deleteByReceiverId(UUID receiverId);
    void deleteBySenderId(UUID senderId);
    Boolean existsBySenderIdAndReceiverId(UUID senderId, UUID receiverId);
    Boolean existsByReceiverId(UUID receiverId);
    Boolean existsBySenderId(UUID senderId);
}
