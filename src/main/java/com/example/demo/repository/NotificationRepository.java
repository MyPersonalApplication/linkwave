package com.example.demo.repository;

import com.example.demo.model.Notification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface NotificationRepository extends BaseRepository<Notification>, JpaSpecificationExecutor<Notification> {
    @Query("SELECT n FROM Notification n WHERE n.receiver.id = :receiverId order by n.createdAt desc")
    List<Notification> findAllByReceiverId(UUID receiverId);
}
