package com.mango.diary.diary.service;

import com.mango.diary.common.enums.Emotion;
import com.mango.diary.diary.domain.AiComment;
import com.mango.diary.diary.domain.Diary;
import com.mango.diary.diary.domain.DiaryStatus;
import com.mango.diary.diary.dto.AiCommentRequest;
import com.mango.diary.diary.dto.AiCommentResponse;
import com.mango.diary.diary.repository.AiCommentRepository;
import com.mango.diary.diary.repository.DiaryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class GeminiService {

    private final DiaryRepository diaryRepository;
    private final AiCommentRepository aiCommentRepository;

    private final RestTemplate restTemplate;

    @Value("${gemini.api.key}")
    private String GEMINI_API_KEY;

    @Value("${gemini.api.url}")
    private String GEMINI_API_URL;

    @Value("${gemini.prompt.emotion}")
    private String GEMINI_API_EMOTION_TEMPLATE;

    @Value("${gemini.prompt.advice}")
    private String GEMINI_API_ADVICE_TEMPLATE;

    public List<Emotion> analyzeEmotion(String diary) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        String prompt = GEMINI_API_EMOTION_TEMPLATE + "\n" + diary;

        GeminiRequest request = new GeminiRequest(prompt);

        ResponseEntity<GeminiResponse> response = getGeminiResponseResponseEntity(request, headers);


        String emotionsText = getGeminiResponseResponseEntity(request, headers)
                .getBody()
                .candidates().get(0)
                .content().parts()
                .get(0).text().trim();

        List<String> emotionsList = Arrays.asList(emotionsText.split(",\\s*"));

        List<Emotion> topEmotions = emotionsList.stream()
                .map(Emotion::valueOf)
                .collect(Collectors.toList());

        return topEmotions;
    }



    private ResponseEntity<GeminiResponse> getGeminiResponseResponseEntity(GeminiRequest request, HttpHeaders headers) {
        HttpEntity<GeminiRequest> entity = new HttpEntity<>(request, headers);

        String url = UriComponentsBuilder.fromHttpUrl(GEMINI_API_URL)
                .queryParam("key", GEMINI_API_KEY)
                .toUriString();

        ResponseEntity<GeminiResponse> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                GeminiResponse.class);
        return response;
    }

    @Transactional
    public AiCommentResponse getAiComment(AiCommentRequest aiCommentRequest) {
        Diary diary = diaryRepository.findById(aiCommentRequest.diaryId())
                .orElseThrow(() -> new RuntimeException("Diary not found"));


        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        String prompt = GEMINI_API_ADVICE_TEMPLATE + "\n" +
                "감정은 " + aiCommentRequest.emotion() +
                "이고. 일기내용은 " + diary.getContent() + "이야";

        GeminiRequest request = new GeminiRequest(prompt);

        ResponseEntity<GeminiResponse> response = getGeminiResponseResponseEntity(request, headers);


        String aiResponse = getGeminiResponseResponseEntity(request, headers)
                .getBody()
                .candidates().get(0)
                .content().parts()
                .get(0).text();

        AiComment aiComment = AiComment.builder()
                .content(aiResponse)
                .diary(diary)
                .build();

        aiCommentRepository.save(aiComment);

        diary.setStatus(DiaryStatus.COMPLETE);
        diary.setEmotion(aiCommentRequest.emotion());

        return new AiCommentResponse(aiComment.getContent());
    }
}
