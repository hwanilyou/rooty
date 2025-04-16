package com.example.userservice.service;


import com.example.userservice.domain.User;
import com.example.userservice.domain.UserBookingEntity;
import com.example.userservice.dto.RegisterRequest;
import com.example.userservice.dto.UserRequest;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.repository.userBookingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final userBookingRepository userBookingRepository;
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

    public void saveBooking(UserRequest userRequest) {
        log.info("메시지 큐에서 받은 예약 저장: 유저={}, 비행기={}", userRequest.getUserId(), userRequest.getFlightId());

        userRequest.getSeats().forEach(seat -> {
            log.info("좌석: {}, 등급: {}, 가격: {}", seat.getSeatName(), seat.getSeatClass(), seat.getPrice());
            // 실제로는 repository.save() 로 저장
            UserBookingEntity booking = new UserBookingEntity();
            booking.setUserId(userRequest.getUserId());
            booking.setFlightId(userRequest.getFlightId());
            booking.setSeatName(seat.getSeatName());
            booking.setSeatClass(seat.getSeatClass());
            booking.setPrice(seat.getPrice());


            log.info("메시지 큐에서 받은 예약 저장: userId={}, flightId={}, seats={}",
                userRequest.getUserId(),
                userRequest.getFlightId(),
                userRequest.getSeats());

            userBookingRepository.save(booking);
        });
    }



}
