package edu.du.seatservice.service;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeatService {

    public List<String> getReservedSeatsByFlightId(Long flightId) {
        // 나중에 DB 연동 예정
        return List.of("1A", "2B", "3C"); // 임시로 예약된 좌석 목록 반환
    }

    //예약 저장 로직
    public void bookSeats(List<String> seats) {
        // 실제로는 DB에 저장해야 함
        System.out.println("예약된 좌석: " + seats);
    }
}