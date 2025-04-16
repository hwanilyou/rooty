package com.example.userservice.consumer;

import com.example.userservice.domain.UserBookingEntity;
import com.example.userservice.dto.UserRequest;
import com.example.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

// 메세지 받기
@Slf4j
@Component
@RequiredArgsConstructor
public class UserConsumer {

    private final UserService userService;

    @RabbitListener(queues = "booking.queue")
    public void receiveBooking(UserRequest userRequest) {
        log.info("Received booking: {}", userRequest);
        userService.saveBooking(userRequest);
    }
}
