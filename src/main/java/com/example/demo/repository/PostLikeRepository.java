package com.example.demo.repository;

import com.example.demo.model.interact.Post;
import com.example.demo.model.interact.PostLike;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface PostLikeRepository extends BaseRepository<PostLike>, JpaSpecificationExecutor<PostLike> {
    @Query("SELECT pl FROM PostLike pl WHERE pl.post.id = :postId")
    List<PostLike> findAllByPostId(UUID postId);
}
