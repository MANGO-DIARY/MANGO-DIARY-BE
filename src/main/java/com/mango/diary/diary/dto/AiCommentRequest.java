package com.mango.diary.diary.dto;

import com.mango.diary.common.enums.Emotion;

public record AiCommentRequest(
        Long diaryId,
        Emotion emotion
) {
}
