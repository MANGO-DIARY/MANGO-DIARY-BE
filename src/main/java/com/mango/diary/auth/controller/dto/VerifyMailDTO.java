package com.mango.diary.auth.controller.dto;

public record VerifyMailDTO(
        String userEmail,
        String code
) {
}
