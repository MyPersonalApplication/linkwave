package com.example.demo.repository;

import com.example.demo.model.interact.Post;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PostRepository extends BaseRepository<Post>, JpaSpecificationExecutor<Post> {
}
