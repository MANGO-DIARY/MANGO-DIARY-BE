package com.mango.diary.common.enums;


import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Getter
public enum Emotion {
    JOY(0),      // 기쁨
    EXCITEMENT(1), // 신남
    HAPPINESS(2), // 행복
    CALM(3),      // 평온
    DEPRESSION(4), // 우울
    ANXIETY(5),   // 불안
    SADNESS(6),   // 슬픔
    ANGER(7);     // 분노

    private final int code;

    @JsonValue
    public int getCode() {
        return code;
    }

    public static Emotion fromCode(int code) {
        for (Emotion emotion : Emotion.values()) {
            if (emotion.getCode() == code) {
                return emotion;
            }
        }
        throw new IllegalArgumentException("Invalid code: " + code);
    }
}

