package com.mango.diary.common.enums;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Getter
public enum Emotion {
    HAPPY("기쁨"),
    FUN("신남"),
    PEACEFUL("평온"),
    LOVE("설렘"),
    SAD("슬픔"),
    ANGRY("분노"),
    FEAR("두려움"),
    LONELINESS("외로움");

    private final String emotion;

    private static final Map<String, Emotion> BY_EMOTION = new HashMap<>();

    static {
        for (Emotion e : values()) {
            BY_EMOTION.put(e.emotion, e);
        }
    }

    public String changeString () {
        return emotion;
    }
}

