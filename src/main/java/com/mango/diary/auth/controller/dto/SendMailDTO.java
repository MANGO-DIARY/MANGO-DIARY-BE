package com.mango.diary.auth.controller.dto;

public record SendMailDTO(
        String userEmail,
        VerificationPurpose purpose
) {
}
