package com.mango.diary.diary.exception;

import com.mango.diary.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DiaryErrorCode implements ErrorCode {
    DIARY_ENTRY_ALREADY_EXISTS(409, 3000, "이미 해당 날짜에 작성된 일기가 있습니다."),
    AI_COMMENT_NOT_FOUND(500, 3001, "AI 코멘트를 찾을 수 없습니다."),
    DIARY_NOT_FOUND(404, 3002, "일기를 찾을 수 없습니다."),
    INVALID_CONTENT(400, 3003, "일기 내용이 올바르지 않습니다."),
    INVALID_DATE(400, 3004, "일기 날짜가 올바르지 않습니다."),
    INVALID_EMOTION(400, 3005, "일기 감정이 올바르지 않습니다."),
    INVALID_EMOTION_TYPE(400, 3006, "감정 타입이 올바르지 않습니다."),
    DIARY_NOT_FOUND_BY_EMOTION(404, 3008, "감정에 해당하는 일기를 찾을 수 없습니다."),
    INVALID_DATE_FORMAT(400, 3009, "날짜 형식이 올바르지 않습니다."),
    LIST_IS_NULL(400, 3010, "리스트가 비어있습니다."),
    //----------------- AI -----------------
    DIARY_ANALYSIS_FAILED(400, 4000, "감정을 분석할 수 없는 일기입니다."),
    //----------------- TODAY COMMENT -----------------
    TODAY_COMMENT_NOT_FOUND(404, 6000, "오늘의 한마디를 찾을 수 없습니다."),;


    private final int statusCode;
    private final int exceptionCode;
    private final String message;
}
