package com.example.notice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * 게시글 Entity 클래스 (DB 테이블 매핑)
 */
@Entity
@Getter
@Setter
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 증가 ID
    private Long id;

    @Column(nullable = false)
    private String title; // 게시글 제목

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content; // 게시글 본문

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt; // 수정 시간
}
