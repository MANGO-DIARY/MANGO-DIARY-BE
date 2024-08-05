package com.mango.diary.statistics.dto;


import java.time.YearMonth;
import java.util.List;

public record StatisticsResponse(
        YearMonth yearMonth,
        EmotionCounts emotionCounts,
        List<String> aiComments,
        String statisticsComment
) {
}
