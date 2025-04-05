package com.example.flightservice.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 항공사 이름
    private String airline;

    // 출발 시간 (LocalDateTime 형식)
    private LocalDateTime departureTime;

    // 도착 시간
    private LocalDateTime arrivalTime;

    // 출발 공항 또는 지역 코드
    private String departureLoc;

    // 도착 공항 또는 지역 코드
    private String arrivalLoc;
}