package com.example.bookingservice.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SeatUpdateRequest {
    private Long reservationId;
    private String newSeat;
}
