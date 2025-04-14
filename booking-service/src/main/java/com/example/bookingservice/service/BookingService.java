package com.example.bookingservice.service;

import com.example.bookingservice.dto.BookingRequest;
import com.example.bookingservice.dto.FlightResponse;
import com.example.bookingservice.dto.SeatUpdateRequest;
import com.example.bookingservice.entity.BookingEntity;
import com.example.bookingservice.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Slf4j
@Service
public class BookingService {

    private final BookingRepository bookingRepository;

    public void bookSeats(BookingRequest bookingRequest) {
        bookingRequest.getSeats().forEach(seat -> {
            System.out.println("좌석: " + seat.getSeatName() + ", 등급: " + seat.getSeatClass() + ", 가격: " + seat.getPrice());
            // 실제로는 repository.save() 로 저장
        });
    }

    public void saveBooking(BookingRequest bookingRequest) {
        log.info("메시지 큐에서 받은 예약 저장: 유저={}, 비행기={}", bookingRequest.getUserId(), bookingRequest.getFlightId());

        bookingRequest.getSeats().forEach(seat -> {
            log.info("좌석: {}, 등급: {}, 가격: {}", seat.getSeatName(), seat.getSeatClass(), seat.getPrice());
            // 실제로는 repository.save() 로 저장
            BookingEntity booking = new BookingEntity();
            booking.setUserId(bookingRequest.getUserId());
            booking.setFlightId(bookingRequest.getFlightId());
            booking.setSeatName(seat.getSeatName());
            booking.setSeatClass(seat.getSeatClass());
            booking.setPrice(seat.getPrice());


            log.info("메시지 큐에서 받은 예약 저장: userId={}, flightId={}, seats={}",
                    bookingRequest.getUserId(),
                    bookingRequest.getFlightId(),
                    bookingRequest.getSeats());

            bookingRepository.save(booking);
        });
    }

    public FlightResponse getFlightDetails(Long flightId) {
        String url = "http://localhost:8001/api/flights/" + flightId;

        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<FlightResponse> response = restTemplate.getForEntity(url, FlightResponse.class);
            return response.getBody();
        } catch (Exception e) {
            log.error("항공편 정보 조회 실패", e);
            return new FlightResponse("알수없음", "알수없음");
        }
    }

    public void updateSeat(SeatUpdateRequest request) {
        BookingEntity booking = bookingRepository.findById(request.getReservationId())
                .orElseThrow(() -> new RuntimeException("예약 정보를 찾을 수 없습니다."));

        booking.setSeatName(request.getNewSeat());
        bookingRepository.save(booking);
    }




}
