package com.example.notice.service;

import com.example.notice.model.Notice;
import com.example.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 조회 기본 설정
public class NoticeService {

    private final NoticeRepository noticeRepository;

    // 🔍 전체 목록 페이징 조회
    public Page<Notice> findAll(int page, int size) {
        return noticeRepository.findAll(
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"))
        );
    }

    // 🔍 단건 조회
    public Notice findById(Long id) {
        return noticeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
    }

    // ✅ 게시글 등록
    @Transactional
    public Notice save(Notice notice) {
        return noticeRepository.save(notice);
    }
}
