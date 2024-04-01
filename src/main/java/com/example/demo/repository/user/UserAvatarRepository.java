package com.example.demo.repository.user;

import com.example.demo.model.user.UserAvatar;
import com.example.demo.repository.BaseRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface UserAvatarRepository extends BaseRepository<UserAvatar>, JpaSpecificationExecutor<UserAvatar> {
    @Query("select ua from UserAvatar ua where ua.user.id = ?1")
    UserAvatar findByUserId(UUID userId);
}
