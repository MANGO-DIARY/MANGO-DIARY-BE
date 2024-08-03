package com.mango.diary.auth.controller.dto;

public record GetUserResponse(
        String userName,
        String userEmail
) {
}
