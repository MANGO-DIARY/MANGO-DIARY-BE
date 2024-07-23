package com.mango.diary.common.exception.dto;

import java.time.LocalDateTime;

public record ErrorResponse(
        int status,
        int exceptionCode,
        String message,
        LocalDateTime timestamp
){
}