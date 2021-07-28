package com.example.mygram.entity;

import javax.persistence.PrePersist;
import java.time.LocalDateTime;

public class Comment {

    private long id;
    private Post post;
    private String username;
    private String message;
    private long userId;
    private LocalDateTime dateCreated;

    @PrePersist
    protected void onCreate() {
        dateCreated = LocalDateTime.now();
    }

}
