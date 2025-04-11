package com.example.notice.service;

import com.example.notice.entity.Post;
import com.example.notice.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 게시글 관련 비즈니스 로직 처리
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;

    // 게시글 목록 조회 (페이지 + 정렬)
    public Page<Post> findAll(int page) {
        return postRepository.findAllByOrderByCreatedAtDesc(
                PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "createdAt"))
        );
    }

    // 단일 게시글 조회
    public Post findById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
    }

    // 게시글 저장
    @Transactional
    public Post save(Post post) {
        return postRepository.save(post);
    }

    // 게시글 수정
    @Transactional
    public Post update(Long id, Post post) {
        Post existingPost = findById(id);
        existingPost.setTitle(post.getTitle());
        existingPost.setContent(post.getContent());
        return postRepository.save(existingPost);
    }

    // 게시글 삭제
    @Transactional
    public void delete(Long id) {
        postRepository.deleteById(id);
    }
}
