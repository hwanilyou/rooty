package com.example.userservice.repository;

import com.example.userservice.domain.UserBookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface userBookingRepository extends JpaRepository<UserBookingEntity, Long> {
}
