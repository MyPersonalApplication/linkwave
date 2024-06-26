package com.example.demo.repository;

import com.example.demo.model.interact.LikeComment;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LikeCommentRepository extends BaseRepository<LikeComment>, JpaSpecificationExecutor<LikeComment> {
    @Query("SELECT l FROM LikeComment l WHERE l.postComment.id = :postCommentId")
    List<LikeComment> findAllByPostCommentId(UUID postCommentId);
}
