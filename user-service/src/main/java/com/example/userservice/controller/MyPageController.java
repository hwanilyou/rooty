package com.example.userservice.controller;

import com.example.userservice.domain.User;
import com.example.userservice.jwt.JwtUtil;
import com.example.userservice.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mypage")
public class MyPageController {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public MyPageController(UserRepository userRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public ResponseEntity<?> getMyPage(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            String username = jwtUtil.extractUsername(token);
            return userRepository.findByUsername(username)
                    .map(user -> ResponseEntity.ok().body(user))
                    .orElse(ResponseEntity.notFound().build());
        }
        return ResponseEntity.status(401).body("Unauthorized");
    }

    @PutMapping
    public ResponseEntity<?> updateMyPage(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestBody User updatedUser
    ) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            String username = jwtUtil.extractUsername(token);

            return userRepository.findByUsername(username)
                    .map(user -> {
                        user.setEmail(updatedUser.getEmail());
                        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                            user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
                        }
                        userRepository.save(user);
                        return ResponseEntity.ok().body("사용자 정보가 수정되었습니다.");
                    })
                    .orElse(ResponseEntity.notFound().build());
        }
        return ResponseEntity.status(401).body("Unauthorized");
    }

    @DeleteMapping
    public ResponseEntity<?> deleteMyAccount(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            String username = jwtUtil.extractUsername(token);

            return userRepository.findByUsername(username)
                    .map(user -> {
                        userRepository.delete(user);
                        return ResponseEntity.ok().body("계정이 삭제되었습니다.");
                    })
                    .orElse(ResponseEntity.notFound().build());
        }
        return ResponseEntity.status(401).body("Unauthorized");
    }
}
