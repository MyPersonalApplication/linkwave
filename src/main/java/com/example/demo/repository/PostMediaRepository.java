package com.example.demo.repository;

import com.example.demo.model.interact.PostMedia;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PostMediaRepository extends BaseRepository<PostMedia>, JpaSpecificationExecutor<PostMedia> {
}
