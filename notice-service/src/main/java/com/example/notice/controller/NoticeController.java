package com.example.notice.controller;

import com.example.notice.model.Notice;
import com.example.notice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 공지사항 관련 REST API 컨트롤러
 */
@RestController
@RequestMapping("/api/notice") // 모든 요청 URL은 /api/notice로 시작
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;


    // 🔍 전체 공지사항 목록 조회 (페이징)
    @GetMapping
    public ResponseEntity<Page<Notice>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(noticeService.findAll(page, size));
    }

    // 🔍 공지사항 하나만 ID로 조회
    @GetMapping("/{id}")
    public ResponseEntity<Notice> findById(@PathVariable Long id) {
        return ResponseEntity.ok(noticeService.findById(id));
    }

    // 📝 공지사항 등록 (POST)
    @PostMapping
    public ResponseEntity<Notice> create(@RequestBody Notice notice) {
        return ResponseEntity.ok(noticeService.save(notice));
    }


}
