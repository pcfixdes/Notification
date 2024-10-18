package com.notification.test.notification.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Entity
@Data
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private String channel;
    private String message;
    private String timestamp;
    private boolean delivered;

    @ElementCollection
    private List<String> subscribedCategories;

}

