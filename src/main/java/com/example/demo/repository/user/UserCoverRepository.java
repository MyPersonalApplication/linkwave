package com.example.demo.repository.user;

import com.example.demo.model.user.UserCover;
import com.example.demo.repository.BaseRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface UserCoverRepository extends BaseRepository<UserCover>, JpaSpecificationExecutor<UserCover> {
    @Query("select uc from UserCover uc where uc.user.id = ?1")
    UserCover findByUserId(UUID userId);
}
