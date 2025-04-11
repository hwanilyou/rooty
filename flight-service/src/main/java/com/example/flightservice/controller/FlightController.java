package com.example.flightservice.controller;

import com.example.flightservice.domain.Flight;
import com.example.flightservice.service.FlightService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/flights")
@RequiredArgsConstructor
public class FlightController {

    private final FlightService flightService;

    // 전체 항공편 조회
    @GetMapping
    public ResponseEntity<List<Flight>> getAllFlights() {
        return ResponseEntity.ok(flightService.getAllFlights());
    }

    // 특정 항공편 조회
    @GetMapping("/{id}")
    public ResponseEntity<Flight> getFlightById(@PathVariable Long id) {
        return ResponseEntity.ok(flightService.getFlightById(id));
    }

    // 신규 항공편 등록
    @PostMapping
    public ResponseEntity<Flight> createFlight(@RequestBody Flight flight) {
        return ResponseEntity.ok(flightService.saveFlight(flight));
    }

    // 항공편 수정 (여기서 수정 처리)
    @PostMapping("/{id}")
    public ResponseEntity<Flight> updateFlightPost(
            @PathVariable Long id,
            @RequestBody Flight flightDetails) {
        Flight updated = flightService.updateFlight(id, flightDetails);
        return ResponseEntity.ok(updated);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Flight> updateFlight(@PathVariable Long id,
                                               @RequestBody Flight flightDetails) {
        Flight updated = flightService.updateFlight(id, flightDetails);
        return ResponseEntity.ok(updated);
    }
    // 항공편 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFlight(@PathVariable Long id) {
        flightService.deleteFlight(id);
        return ResponseEntity.ok().build();
    }
}