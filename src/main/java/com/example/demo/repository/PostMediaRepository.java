package com.example.demo.repository;

import com.example.demo.model.interact.PostMedia;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PostMediaRepository extends BaseRepository<PostMedia>, JpaSpecificationExecutor<PostMedia> {
}
