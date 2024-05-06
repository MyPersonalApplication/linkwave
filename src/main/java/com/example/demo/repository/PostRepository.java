package com.example.demo.repository;

import com.example.demo.model.interact.Post;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends BaseRepository<Post>, JpaSpecificationExecutor<Post> {
    @Query("select p from Post p where p.archived = false order by p.createdAt desc")
    List<Post> findAllPosts();
}
