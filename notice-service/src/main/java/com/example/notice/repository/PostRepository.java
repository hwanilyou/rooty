package com.example.notice.repository;

import com.example.notice.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 게시글 관련 DB 접근 인터페이스 (JPA 제공)
 */
public interface PostRepository extends JpaRepository<Post, Long> {

    // 생성일 내림차순으로 정렬된 게시글 페이징 조회
    Page<Post> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
