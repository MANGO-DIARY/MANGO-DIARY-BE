package com.mango.diary.diary.dto;

import com.mango.diary.common.enums.Emotion;

import java.time.LocalDate;

public record DiaryForCalenderResponse(
        Long id,
        LocalDate date,
        Emotion emotion
) {
}
