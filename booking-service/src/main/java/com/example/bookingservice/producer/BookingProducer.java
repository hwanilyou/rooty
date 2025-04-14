package com.example.bookingservice.producer;

import com.example.bookingservice.dto.BookingRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

// 메세지 보내기
@Component
@RequiredArgsConstructor
public class BookingProducer {

    private final RabbitTemplate rabbitTemplate;

    private static final String EXCHANGE_NAME = "booking.exchange";
    private static final String ROUTING_KEY = "booking.key";

    public void sendBooking(BookingRequest bookingRequest) {
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, bookingRequest);
    }
}