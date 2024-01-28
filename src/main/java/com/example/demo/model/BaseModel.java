package com.example.demo.model;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.sql.Timestamp;
import java.util.Date;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class BaseModel {
    @Column(name = "archived", columnDefinition = "boolean default false")
    protected boolean archived;

    @Column(name = "created_at", columnDefinition = "timestamp default now()")
    protected Timestamp createdAt;

    @Column(name = "updated_at", columnDefinition = "timestamp default now()")
    protected Timestamp updatedAt;
}
