package com.mango.diary.diary.dto;

import com.mango.diary.common.enums.Emotion;

import java.util.List;

public record AiEmotionResponse(
    Long diaryId,
    List<Emotion> emotions
){
}
