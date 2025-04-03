package com.example.board.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Table(name = "users")

public class UserEntity {
    @Id // primary key 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long no;

    @Column(nullable = false, unique = true, length = 50)
    private String id;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 20)
    private String role;  // 사용자 권한 ( 일반 user 혹은 admin 관리자 )

    @Column
    private LocalDateTime createdAt = LocalDateTime.now();

}
