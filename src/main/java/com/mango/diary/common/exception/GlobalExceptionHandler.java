package com.mango.diary.common.exception;

import com.mango.diary.common.exception.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handleBaseException(BaseException e){
        ErrorCode errorCode = e.getErrorCode();
        ErrorResponse errorResponse = new ErrorResponse(
                errorCode.getStatusCode(),
                errorCode.getExceptionCode(),
                errorCode.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity
                .status(HttpStatus.valueOf(errorCode.getStatusCode()))
                .body(errorResponse);
    }
}