package com.mango.diary.diary.exception;


import com.mango.diary.common.exception.BaseException;

public class DiaryException extends BaseException {

    public DiaryException(DiaryErrorCode diaryErrorCode) {
        super(diaryErrorCode);
    }
}
