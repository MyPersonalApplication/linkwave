package com.example.demo.repository;

import com.example.demo.model.interact.PostComment;
import com.example.demo.model.interact.ReplyComment;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ReplyCommentRepository extends BaseRepository<ReplyComment>, JpaSpecificationExecutor<ReplyComment> {
    @Query("SELECT rc FROM ReplyComment rc WHERE rc.postComment.id = :postCommentId")
    List<ReplyComment> findByPostCommentId(UUID postCommentId);
}
