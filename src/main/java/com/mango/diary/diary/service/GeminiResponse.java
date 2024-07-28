package com.mango.diary.diary.service;

import java.util.List;

public record GeminiResponse(List<Candidate> candidates) {

    public static record Candidate(Content content) {}

    public static record Content(List<Parts> parts) {}

    public static record Parts(String text) {}
}
