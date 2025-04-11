package com.example.notice.repository;

import com.example.notice.model.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {
    // JPA가 자동 구현: findAll, findById, save 등 기본 메서드 제공
}
