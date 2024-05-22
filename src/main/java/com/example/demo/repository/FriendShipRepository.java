package com.example.demo.repository;

import com.example.demo.model.friend.Friendship;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FriendShipRepository extends BaseRepository<Friendship>, JpaSpecificationExecutor<Friendship> {
    List<Friendship> findByUserId(UUID userId);

    List<Friendship> findByFriendId(UUID friendId);

    Friendship findByUserIdAndFriendId(UUID userId, UUID friendId);

    void deleteById(UUID friendShipId);
}
