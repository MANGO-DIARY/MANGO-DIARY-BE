package com.mango.diary.statistics.dto;

import com.mango.diary.statistics.entity.EmotionStatistics;

import java.time.YearMonth;

public record StatisticsResponse(
        YearMonth yearMonth,
        Long 기쁨,
        Long 신남,
        Long 행복,
        Long 평온,
        Long 슬픔,
        Long 분노,
        Long 불안,
        Long 우울,
        String monthlyComment,
        String statisticsComment
) {
}
