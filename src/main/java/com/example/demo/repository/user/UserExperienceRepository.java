package com.example.demo.repository.user;

import com.example.demo.model.user.UserExperience;
import com.example.demo.repository.BaseRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface UserExperienceRepository extends BaseRepository<UserExperience>, JpaSpecificationExecutor<UserExperience> {
    @Query("select ue from UserExperience ue where ue.user.id = ?1")
    List<UserExperience> findByUserId(UUID userId);
}
