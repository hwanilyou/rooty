package com.example.bookingservice.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponse {
    private Long id;
    private Long flightId;
    private String seatClass;
    private String seatName;
    private String departureLoc;
    private String arrivalLoc;
}
