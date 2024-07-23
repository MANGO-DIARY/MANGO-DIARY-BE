package com.mango.diary.common.exception;

import lombok.Getter;

@Getter
public class BaseException extends Throwable {
    private final ErrorCode errorCode;

    public BaseException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}