package com.example.mygram.entity;

import javax.persistence.PrePersist;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Post {

    private long id;
    private String title;
    private String caption;
    private String location;
    private int likes;

    private User user;
    private Set<String> likeUsers = new HashSet<>();
    private List<Comment> comments = new ArrayList<>();
    private LocalDateTime dateCreated;

    @PrePersist
    protected void onCreate() {
        dateCreated = LocalDateTime.now();
    }

}
