package com.mango.diary.diary.dto;

import com.mango.diary.common.enums.Emotion;

public record AiCommentRequest(
        String diaryContent,
        Emotion emotion
) {
}
