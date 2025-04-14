package com.example.bookingservice.controller;

import com.example.bookingservice.dto.BookingRequest;
import com.example.bookingservice.dto.FlightResponse;
import com.example.bookingservice.dto.SeatUpdateRequest;
import com.example.bookingservice.entity.BookingEntity;
import com.example.bookingservice.repository.BookingRepository;
import com.example.bookingservice.service.BookingService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;


import java.security.Key;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("") // StripPrefix=2에 맞게 루트에 배치
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final BookingRepository bookingRepository;

    @Value("${jwt.secret}")
    private String jwtSecret; // ✅ JWT 검증용 키

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    @PostMapping("/book-seats")
    public ResponseEntity<String> bookSeats(@RequestBody BookingRequest bookingRequest) {
        log.info("예약 요청 들어옴: 유저={}, 비행기={}", bookingRequest.getUserId(), bookingRequest.getFlightId());
        log.info("Booking request: {}", bookingRequest);

        try {
            bookingService.bookSeats(bookingRequest);
            bookingService.saveBooking(bookingRequest);
            return ResponseEntity.ok("예약이 성공적으로 완료되었습니다!");
        } catch (Exception e) {
            log.error("예약 처리 중 오류 발생", e);
            return ResponseEntity.status(500).body("예약 처리 중 오류가 발생했습니다.");
        }
    }

    @GetMapping("/reserved-seats")
    public List<String> getReservedSeats(@RequestParam Long flightId) {
        return bookingRepository.findByFlightId(flightId).stream()
                .map(BookingEntity::getSeatName)
                .toList();
    }


    // ✅ 현재 로그인된 사용자 본인의 예약 목록
    @GetMapping("/my")
    public ResponseEntity<List<BookingEntity>> getMyReservations(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");

            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8)))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            Long userId = claims.get("id", Integer.class).longValue(); // 👍 맞는 키!

            log.info("요청 사용자 ID: {}", userId);

            List<BookingEntity> myBookings = bookingRepository.findByUserId(userId);
            return ResponseEntity.ok(myBookings);

        } catch (Exception e) {
            log.error("토큰 파싱 실패:", e);
            return ResponseEntity.status(401).build(); // 인증 실패
        }
    }


    // 사용자 ID로 예약 내역 조회
    @GetMapping("/bookings/user/{userId}")
    public List<BookingEntity> getBookingsByUserId(@PathVariable Long userId) {
        return bookingRepository.findByUserId(userId);
    }

    // ✨ RestTemplate 호출 메서드
    private FlightResponse getFlightInfoViaRestTemplate(Long flightId) {
        String url = "http://localhost:8001/api/flights/" + flightId;

        try {
            RestTemplate restTemplate = new RestTemplate();
            return restTemplate.getForObject(url, FlightResponse.class);
        } catch (Exception e) {
            log.warn("⚠️ 항공편 정보 호출 실패, 기본값으로 대체: " + flightId);
            return new FlightResponse("출발지 알 수 없음", "도착지 알 수 없음");
        }
    }

    // ✅ 단건 예약 정보 조회 (예약 수정용)
    @GetMapping("/bookings/{id}")
    public ResponseEntity<BookingEntity> getBookingById(@PathVariable Long id) {
        return bookingRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @GetMapping("/{id}") // ✅ 이렇게 바꿈
    public BookingEntity getBooking(@PathVariable Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("예약 정보를 찾을 수 없습니다."));
    }


    @PutMapping("/bookings/update-seat")
    public ResponseEntity<String> updateSeat(@RequestBody SeatUpdateRequest request) {
        try {
            bookingService.updateSeat(request);
            return ResponseEntity.ok("좌석이 성공적으로 변경되었습니다.");
        } catch (Exception e) {
            log.error("좌석 변경 중 오류 발생", e);
            return ResponseEntity.status(500).body("좌석 변경 실패");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelBooking(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        try {
            // JWT에서 사용자 정보 파싱 (선택: 삭제 권한 확인용)
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8)))
                    .build()
                    .parseClaimsJws(token.replace("Bearer ", ""))
                    .getBody();

            Long userIdFromToken = claims.get("id", Integer.class).longValue();

            // 예약 정보 존재 여부 확인
            BookingEntity booking = bookingRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("예약 정보를 찾을 수 없습니다."));

            // 로그인한 유저가 예약자 또는 관리자(추후 role 확인 시)만 삭제 가능
            if (!booking.getUserId().equals(userIdFromToken)) {
                return ResponseEntity.status(403).body("예약 취소 권한이 없습니다.");
            }

            // 삭제 수행
            bookingRepository.deleteById(id);
            return ResponseEntity.ok("예약이 취소되었습니다.");

        } catch (Exception e) {
            log.error("예약 취소 중 오류 발생", e);
            return ResponseEntity.status(500).body("예약 취소에 실패했습니다.");
        }
    }

    @GetMapping("/admin/bookings")
    public ResponseEntity<List<BookingEntity>> getAllBookingsForAdmin(@RequestHeader("Authorization") String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8)))
                    .build()
                    .parseClaimsJws(token.replace("Bearer ", ""))
                    .getBody();

            String role = claims.get("role", String.class);
            if (!"ADMIN".equalsIgnoreCase(role)) {
                return ResponseEntity.status(403).body(null);
            }

            List<BookingEntity> allBookings = bookingRepository.findAll();
            return ResponseEntity.ok(allBookings);

        } catch (Exception e) {
            log.error("관리자 전체 예약 조회 실패", e);
            return ResponseEntity.status(500).build();
        }
    }








}

