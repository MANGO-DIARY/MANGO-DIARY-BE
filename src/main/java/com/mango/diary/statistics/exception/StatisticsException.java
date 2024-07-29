package com.mango.diary.statistics.exception;

import com.mango.diary.common.exception.BaseException;

public class StatisticsException extends BaseException {

    public StatisticsException(StatisticsErrorCode errorCode) {
        super(errorCode);
    }
}

