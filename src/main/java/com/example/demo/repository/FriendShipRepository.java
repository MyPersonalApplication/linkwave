package com.example.demo.repository;

import com.example.demo.model.friend.Friendship;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface FriendShipRepository extends BaseRepository<Friendship>, JpaSpecificationExecutor<Friendship> {
    List<Friendship> findByUserId(UUID userId);

    List<Friendship> findByFriendId(UUID friendId);

    Friendship findByUserIdAndFriendId(UUID userId, UUID friendId);

    void deleteById(UUID friendShipId);
}
