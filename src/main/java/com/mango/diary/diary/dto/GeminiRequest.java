package com.mango.diary.diary.dto;

import java.util.List;

public record GeminiRequest(List<Content> contents, GenerationConfig generationConfig) {

    public GeminiRequest(String prompt) {
        this(List.of(new Content(new Parts(prompt))), new GenerationConfig(1, 1000, 0.7));
    }

    public static record Content(Parts parts) {}

    public static record Parts(String text) {}

    public static record GenerationConfig(int candidate_count, int max_output_tokens, double temperature) {}
}