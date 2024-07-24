package com.mango.diary.auth.controller.dto;

public record UserDTO(
        String userEmail,
        String password
) {
}
