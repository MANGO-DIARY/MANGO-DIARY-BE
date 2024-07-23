package com.mango.diary.common.exception;

public interface ErrorCode {
    int getStatusCode();
    int getExceptionCode();
    String getMessage();
}
