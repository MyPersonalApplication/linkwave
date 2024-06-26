package com.example.demo.repository.user;

import com.example.demo.model.user.User;
import com.example.demo.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface UserRepository extends BaseRepository<User>, JpaSpecificationExecutor<User> {
    User findByEmail(String email);

    @Query("""
            SELECT u FROM User u
            WHERE u.id <> :excludedId
            """)
    List<User> findUsersExcludingId(UUID excludedId);

    @Query("""
            SELECT u FROM User u
            WHERE u.id <> :excludedId
            """)
    Page<User> findUsersExcludingId(UUID excludedId, Pageable pageable);

    @Override
    List<User> findAll();

    Page<User> findByEmailContainingIgnoreCase(String name, Pageable pageable);
}
