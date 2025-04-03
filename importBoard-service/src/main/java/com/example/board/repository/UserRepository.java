package com.example.board.repository;



import com.example.board.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findById(String id);  // id로 사용자 찾기
    Optional<UserEntity> findByEmail(String email); // email로 사용자 찾기
    Optional<UserEntity> findByName(String name);  // 이름으로 사용자 찾기
}
