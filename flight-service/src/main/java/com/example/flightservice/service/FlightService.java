package com.example.flightservice.service;


import com.example.flightservice.domain.Flight;
import com.example.flightservice.repository.FlightRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FlightService {

    private final FlightRepository flightRepository;

    public List<Flight> getAllFlights() {
        return flightRepository.findAll();
    }

    public Flight getFlightById(Long id) {
        return flightRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Flight not found with id: " + id));
    }

    public Flight saveFlight(Flight flight) {
        return flightRepository.save(flight);
    }

    public Flight updateFlight(Long id, Flight flightDetails) {
        Flight existingFlight = flightRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Flight not found with id: " + id));

        existingFlight.setAirline(flightDetails.getAirline());
        existingFlight.setDepartureTime(flightDetails.getDepartureTime());
        existingFlight.setArrivalTime(flightDetails.getArrivalTime());
        existingFlight.setDepartureLoc(flightDetails.getDepartureLoc());
        existingFlight.setArrivalLoc(flightDetails.getArrivalLoc());

        return flightRepository.save(existingFlight);
    }

    public void deleteFlight(Long id) {
        flightRepository.deleteById(id);
    }
}