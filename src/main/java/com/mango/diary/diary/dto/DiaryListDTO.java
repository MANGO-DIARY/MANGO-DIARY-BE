package com.mango.diary.diary.dto;

import com.mango.diary.common.enums.Emotion;

import java.time.LocalDate;

public record DiaryListDTO(
        Long id,
        String content,
        LocalDate date,
        Emotion emotion
){
}
