package com.mango.diary.statistics.dto;

public record EmotionCounts(
        Long joy,
        Long excitement,
        Long happiness,
        Long calm,
        Long sadness,
        Long anger,
        Long anxiety,
        Long depression
) {
}
