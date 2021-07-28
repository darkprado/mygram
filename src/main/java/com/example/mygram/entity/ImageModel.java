package com.example.mygram.entity;

import lombok.Data;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.*;

@Data
@Entity
public class ImageModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @JsonIgnore
    private long userId;

    @JsonIgnore
    private long postId;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] imageBytes;

}
