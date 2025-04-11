package com.example.userservice.service;

import com.example.userservice.domain.User;
import com.example.userservice.dto.RegisterRequest;
import com.example.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 로그인 유효성 검사 (암호화된 비밀번호 비교 포함)
     */
    public User validateUser(String username, String rawPassword) {
        System.out.println("🛂 로그인 시도 - ID: " + username + ", PW: " + rawPassword);

        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            System.out.println("✅ DB 사용자 존재 - 암호화 PW: " + user.getPassword());

            if (passwordEncoder.matches(rawPassword, user.getPassword())) {
                System.out.println("🎉 비밀번호 일치");
                return user;
            } else {
                System.out.println("❌ 비밀번호 불일치");
            }
        } else {
            System.out.println("❌ 사용자 없음");
        }

        return null;
    }

    /**
     * 회원가입 (비밀번호 암호화 포함)
     */
    public void registerUser(RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("이미 존재하는 사용자입니다.");
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User user = User.builder()
                .username(request.getUsername())
                .password(encodedPassword)
                .role(request.getUsername().toLowerCase().contains("admin") ? "ADMIN" : "USER")
                .name(request.getName())
                .email(request.getEmail())
                .build();
        userRepository.save(user);
    }
}
