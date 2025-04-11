//package com.example.notice.config;
//
//
//import com.example.notice.entity.Post;
//import com.example.notice.repository.PostRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.time.LocalDateTime;
//import java.util.Random;
//
//@Configuration
//@RequiredArgsConstructor
//public class DataInitializer {
//
//    private final PostRepository postRepository;
//
//
//    @Bean
//    public CommandLineRunner initData() {
//        return args -> {
//            // 테스트 게시글 생성
//            for (int i = 1; i <= 10; i++) {
//                Post post = new Post();
//                post.setTitle(i + "시 부산행 아시아나 비행기 ");
//                post.setContent("부산행 비행기가 연착 되었습니다 ");
//                post.setCreatedAt(LocalDateTime.now().minusDays(new Random().nextInt(30)));
//                postRepository.save(post);
//
//
//            }
//        };
//    }
//}