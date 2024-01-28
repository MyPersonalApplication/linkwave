package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.UUID;

@NoRepositoryBean
public interface BaseRepository<T> extends JpaRepository<T, UUID> {
    <Dto> Dto findById(UUID id, Class<Dto> type);

    <Dto> List<Dto> findBy(Class<Dto> type); // find all and map to the desired projection
}
