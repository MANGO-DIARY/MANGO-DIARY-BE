package com.mango.diary.diary.controller;

import com.mango.diary.diary.dto.AiCommentRequest;
import com.mango.diary.diary.dto.AiCommentResponse;
import com.mango.diary.diary.dto.AiEmotionRequest;
import com.mango.diary.diary.dto.AiEmotionResponse;
import com.mango.diary.diary.service.GeminiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/ai")
public class AiCommentController {

    private final GeminiService geminiService;


    @PostMapping("/emotion-analyze")
    public ResponseEntity<AiEmotionResponse> createAiEmotionAnalyze(@RequestBody AiEmotionRequest aiEmotionRequest) {
        return ResponseEntity.ok(geminiService.analyzeEmotion(aiEmotionRequest));
    }

    @PostMapping("/comment")
    public ResponseEntity<AiCommentResponse> getAiComment(@RequestBody AiCommentRequest aiCommentRequest) {
        return ResponseEntity.ok(geminiService.getAiComment(aiCommentRequest));
    }
}
