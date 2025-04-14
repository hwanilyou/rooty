package com.example.bookingservice.dto;

import lombok.Data;

import java.util.List;

@Data
public class BookingRequest {
    private Long userId;
    private Long flightId;
    private List<SeatInfo> seats;

    @Data
    public static class SeatInfo {
        private String seatName;
        private String seatClass;
        private int price;
    }
}

