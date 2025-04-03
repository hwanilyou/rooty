package com.example.board.repository;


import com.example.board.entity.ImportBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImportBoardRepository extends JpaRepository<ImportBoard, Long> {
    //jpa 메서드 이름 규칙 맞게 이름 변경함 이거 안쓰려면 @Query어노테이션 쓰라는데 그냥 테스트용
    List<ImportBoard> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(String name, String content); //타이틀이나 내용 검색기능
}
