package com.mango.diary.auth.controller.dto;

public record SignUpRequestDTO(
        String userEmail,
        String password,
        String userName
) {
}
