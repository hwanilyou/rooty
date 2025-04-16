package com.example.userservice.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class UserRequest {
    private Long userId;
    private Long flightId;
    private List<SeatInfo> seats;

    @Getter
    @Setter
    public static class SeatInfo {
        private String seatName;
        private String seatClass;
        private int price;
    }
}


