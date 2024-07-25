package com.mango.diary.diary.dto;

public record DiaryResponse(
        Long id,
        String content,
        String date,
        String emotion,
        String aiComment,
        Long user
) {
}
