package com.mango.diary.diary.dto;

import com.mango.diary.common.enums.Emotion;

import java.time.LocalDateTime;

public record DiaryRequest(
        String content,
        LocalDateTime date,
        Emotion emotion,
        String aiComment,
        Long user
) {
}
