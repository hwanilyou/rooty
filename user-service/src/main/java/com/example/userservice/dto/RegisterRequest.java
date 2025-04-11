package com.example.userservice.dto;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class RegisterRequest {
    private String username;
    private String password;
    private String name;

    @Email(message = "올바른 이메일 형식이어야 합니다.")
    private String email;
}
