package com.mango.diary.diary.dto;

import com.mango.diary.common.enums.Emotion;

public record DiaryResponse(
        Long id,
        String content,
        String date,
        Emotion emotion,
        String aiComment
) {
}
