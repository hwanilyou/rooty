package edu.du.seatservice.controller;


import edu.du.seatservice.service.SeatService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SeatController {

    private final SeatService seatService;


    @GetMapping("/reserved-seats")
    public List<String> getReservedSeats(@RequestParam Long flightId) {
        return seatService.getReservedSeatsByFlightId(flightId);
    }

    //좌석 예약 요청 받기 post
    @PostMapping("/book-seats")
    public ResponseEntity<String> bookSeats(@RequestBody SeatRequest request) {
        seatService.bookSeats(request.getSeats());
        return ResponseEntity.ok("예약 완료");
    }

    //json 처리용 dto
    @Data
    static class SeatRequest {
        private List<String> seats;
    }



}
