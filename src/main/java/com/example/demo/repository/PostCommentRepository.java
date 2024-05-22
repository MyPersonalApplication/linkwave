package com.example.demo.repository;

import com.example.demo.model.interact.PostComment;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PostCommentRepository extends BaseRepository<PostComment>, JpaSpecificationExecutor<PostComment> {
}
