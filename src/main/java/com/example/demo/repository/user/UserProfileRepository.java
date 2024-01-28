package com.example.demo.repository.user;

import com.example.demo.model.user.UserProfile;
import com.example.demo.repository.BaseRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface UserProfileRepository extends BaseRepository<UserProfile>, JpaSpecificationExecutor<UserProfile> {
    @Query("select up from UserProfile up where up.user.id = ?1")
    UserProfile findByUserId(UUID userId);
}
