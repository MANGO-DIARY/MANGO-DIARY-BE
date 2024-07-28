package com.mango.diary.diary.dto;


import java.time.LocalDate;

public record DiaryRequest(
        String content,
        LocalDate date
) {
}
