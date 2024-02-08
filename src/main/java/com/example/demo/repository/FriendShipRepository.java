package com.example.demo.repository;

import com.example.demo.model.friend.Friendship;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.UUID;

public interface FriendShipRepository extends BaseRepository<Friendship>, JpaSpecificationExecutor<Friendship> {
    List<Friendship> findByUserId(UUID userId);
    List<Friendship> findByFriendId(UUID friendId);
//    Friendship findBySenderIdAndReceiverId(Long senderId, Long receiverId);
//    void deleteBySenderIdAndReceiverId(Long senderId, Long receiverId);
//    void deleteByReceiverId(Long receiverId);
//    void deleteBySenderId(Long senderId);
//    Boolean existsBySenderIdAndReceiverId(Long senderId, Long receiverId);
//    Boolean existsByReceiverId(Long receiverId);
//    Boolean existsBySenderId(Long senderId);
}
