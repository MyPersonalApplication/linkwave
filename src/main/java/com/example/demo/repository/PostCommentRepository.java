package com.example.demo.repository;

import com.example.demo.model.interact.PostComment;
import com.example.demo.model.interact.PostLike;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PostCommentRepository extends BaseRepository<PostComment>, JpaSpecificationExecutor<PostComment> {
}
