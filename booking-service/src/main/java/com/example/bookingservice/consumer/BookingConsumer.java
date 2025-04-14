package com.example.bookingservice.consumer;

import com.example.bookingservice.dto.BookingRequest;
import com.example.bookingservice.service.BookingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

// 메세지 받기
@Slf4j
@Component
@RequiredArgsConstructor
public class BookingConsumer {

    private final BookingService bookingService;

    @RabbitListener(queues = "booking.queue")
    public void receiveBooking(BookingRequest bookingRequest) {
        log.info("Received booking: {}", bookingRequest);
        bookingService.saveBooking(bookingRequest);
    }
}