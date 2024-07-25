package com.mango.diary.diary.exception;

import com.mango.diary.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DiaryErrorCode implements ErrorCode {
    DIARY_ENTRY_LIMIT_EXCEEDED(409, 3000, "일기는 하루에 한 개만 작성할 수 있습니다."),
    AI_COMMENT_NOT_FOUND(500, 3001, "AI 코멘트를 찾을 수 없습니다."),
    DIARY_NOT_FOUND(404, 3002, "일기를 찾을 수 없습니다."),
    INVALID_CONTENT(400, 3003, "일기 내용이 올바르지 않습니다."),
    INVALID_DATE(400, 3004, "일기 날짜가 올바르지 않습니다."),
    INVALID_EMOTION(400, 3005, "일기 감정이 올바르지 않습니다."),
    INVALID_EMOTION_TYPE(400, 3006, "감정 타입이 올바르지 않습니다.")
    ;


    private final int statusCode;
    private final int exceptionCode;
    private final String message;
}
