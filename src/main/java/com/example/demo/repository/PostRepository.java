package com.example.demo.repository;

import com.example.demo.model.interact.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface PostRepository extends BaseRepository<Post>, JpaSpecificationExecutor<Post> {
    @Query("select p from Post p where p.archived = false order by p.createdAt desc")
    List<Post> findAllPosts();

    @Query("select p from Post p where p.archived = false order by p.createdAt desc")
    Page<Post> findAllPosts(Pageable pageable);

    @Query("select p from Post p where p.user.id = :userId and p.archived = false order by p.createdAt desc")
    List<Post> findAllByUserId(UUID userId);

    @Override
    List<Post> findAll();
}
