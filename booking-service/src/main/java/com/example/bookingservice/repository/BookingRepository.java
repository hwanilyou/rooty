package com.example.bookingservice.repository;

import com.example.bookingservice.entity.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<BookingEntity, Long> {
    List<BookingEntity> findByFlightId(Long flightId);

    List<BookingEntity> findByUserId(Long userId);
}