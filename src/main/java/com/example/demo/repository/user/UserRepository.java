package com.example.demo.repository.user;

import com.example.demo.model.user.User;
import com.example.demo.repository.BaseRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface UserRepository extends BaseRepository<User>, JpaSpecificationExecutor<User> {
    User findByEmail(String email);
}
