package com.mango.diary.diary.dto;

import java.util.List;

public record MainPageResponse(
        String todayComment,
        DiaryResponse todayDiary,
        List<EmotionCountDTO> topThreeEmotionThisMonth
) {
}
