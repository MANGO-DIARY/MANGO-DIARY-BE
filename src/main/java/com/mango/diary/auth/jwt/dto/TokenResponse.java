package com.mango.diary.auth.jwt.dto;

public record TokenResponse(
        String accessToken,
        String refreshToken

) {
}
