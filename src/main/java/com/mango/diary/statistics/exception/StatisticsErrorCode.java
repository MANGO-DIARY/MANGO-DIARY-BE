package com.mango.diary.statistics.exception;

import com.mango.diary.common.exception.ErrorCode;
import lombok.Getter;

@Getter
public enum StatisticsErrorCode implements ErrorCode {

    STATISTICS_NOT_FOUND(404, 5000, "해당 달의 통계가 존재하지 않습니다.");


    private final int statusCode;
    private final int exceptionCode;
    private final String message;

    StatisticsErrorCode(int statusCode, int exceptionCode, String message) {
        this.statusCode = statusCode;
        this.exceptionCode = exceptionCode;
        this.message = message;
    }
}
