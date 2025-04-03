package com.example.board.service;


import com.example.board.entity.ImportBoard;
import com.example.board.entity.UserEntity;
import com.example.board.repository.ImportBoardRepository;
import com.example.board.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ImportBoardService {

    private final ImportBoardRepository importBoardRepository;
    private final UserRepository userRepository;

    //공지사항 생성 (관리자일때만)
    public ImportBoard createImportBoard(ImportBoard importBoard, String userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!"ADMIN".equals(user.getRole())) {
            throw new SecurityException("공지사항 작성 권한이 없습니다.");
        }

        importBoard.setUser(user);
        importBoard.setCreatedAt(LocalDateTime.now());
        return importBoardRepository.save(importBoard);
    }

    //페이징 처리
    public List<ImportBoard> getLatestNotices() {
        return importBoardRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"))
                .stream().limit(5).collect(Collectors.toList());
    }

    //페이징 처리
    public Page<ImportBoard> getImportBoardPage(int page) {
        Pageable pageable = PageRequest.of(page, 5, Sort.by(Sort.Direction.DESC, "createdAt"));
        return importBoardRepository.findAll(pageable);
    }

    //관리자임을 확인하기 위함
    public ImportBoard findById(Long no) {
        return importBoardRepository.findById(no)
                .orElse(null);
    }

    // 수정 시 필요한 메서드
    public ImportBoard updateImportBoard(ImportBoard existingBoard, ImportBoard newData) {
        existingBoard.setTitle(newData.getTitle());
        existingBoard.setContent(newData.getContent());
        // views, createdAt 등 필요한 필드는 유지 or 업데이트 로직
        existingBoard.setUpdatedAt(LocalDateTime.now());
        return importBoardRepository.save(existingBoard);
    }

    public ImportBoard findByNo(Long no) {
        return importBoardRepository.findById(no).orElse(null);
    }

    public List<ImportBoard> findAll() {
        return importBoardRepository.findAll();
    }

    //삭제 필요한 메서드
    public void deleteImportBoard(Long no){
        importBoardRepository.deleteById(no);
    }

    //검색 메서드
    public List<ImportBoard> searchNotices(String keyword){
        return importBoardRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(keyword, keyword);
    }

    //조회수 관련 save메서드
    public ImportBoard save(ImportBoard importBoard) {
        return importBoardRepository.save(importBoard);
    }

}