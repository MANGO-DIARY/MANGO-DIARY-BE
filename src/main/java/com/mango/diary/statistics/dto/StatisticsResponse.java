package com.mango.diary.statistics.dto;

import com.mango.diary.statistics.entity.EmotionStatistics;

import java.time.YearMonth;

public record StatisticsResponse(
        YearMonth yearMonth,
        EmotionCounts emotionCounts,
        String monthlyComment,
        String statisticsComment
) {
}
