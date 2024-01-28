package com.example.demo.repository.user;

import com.example.demo.model.user.UserSkill;
import com.example.demo.repository.BaseRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface UserSkillRepository extends BaseRepository<UserSkill>, JpaSpecificationExecutor<UserSkill> {
    @Query("select us from UserSkill us where us.user.id = ?1")
    List<UserSkill> findByUserId(UUID userId);
}
