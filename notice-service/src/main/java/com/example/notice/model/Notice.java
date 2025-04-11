package com.example.notice.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "notice")
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private Long orderId;
}
