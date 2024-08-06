package com.mango.diary.diary.service;

import com.mango.diary.common.enums.Emotion;
import com.mango.diary.diary.dto.*;
import com.mango.diary.diary.exception.DiaryErrorCode;
import com.mango.diary.diary.exception.DiaryException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class GeminiService {

    private final RestTemplate restTemplate;

    @Value("${gemini.api.key}")
    private String GEMINI_API_KEY;

    @Value("${gemini.api.url}")
    private String GEMINI_API_URL;

    @Value("${gemini.prompt.emotion}")
    private String GEMINI_API_EMOTION_TEMPLATE;

    @Value("${gemini.prompt.advice}")
    private String GEMINI_API_ADVICE_TEMPLATE;

    @Value("${gemini.prompt.monthly-comment}")
    private String GEMINI_API_MONTHLY_COMMENT_TEMPLATE;

    public AiEmotionResponse analyzeEmotion(AiEmotionRequest aiEmotionRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        String prompt = GEMINI_API_EMOTION_TEMPLATE + "\n" + "\"" + aiEmotionRequest.diaryContent() + "\"";

        GeminiRequest request = new GeminiRequest(prompt);

        String emotionsText = getGeminiResponseResponseEntity(request, headers)
                .getBody()
                .candidates().get(0)
                .content().parts()
                .get(0).text().trim();

        List<String> emotionsList = Arrays.asList(emotionsText.split(",\\s*"));

        List<Emotion> topEmotions = emotionsList.stream()
                .map(this::parseEmotion)
                .collect(Collectors.toList());

        return new AiEmotionResponse(topEmotions);
    }

    private Emotion parseEmotion(String emotionStr) {
        try {
            return Emotion.valueOf(emotionStr);
        } catch (IllegalArgumentException e) {
            throw new DiaryException(DiaryErrorCode.DIARY_ANALYSIS_FAILED);
        }
    }


    public AiCommentResponse getAiComment(AiCommentRequest aiCommentRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        String prompt = GEMINI_API_ADVICE_TEMPLATE + "\n" +
                "일기내용 :" + aiCommentRequest.diaryContent()  +
                "감정 : " + aiCommentRequest.emotion();

        GeminiRequest request = new GeminiRequest(prompt);

        String aiComment = getGeminiResponseResponseEntity(request, headers)
                .getBody()
                .candidates().get(0)
                .content().parts()
                .get(0).text()
                .replace("\n", "")
                .replaceAll("\\s{2,}", " ");

        return new AiCommentResponse(aiComment);
    }

    public String getMonthlyComment(){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        String prompt = GEMINI_API_MONTHLY_COMMENT_TEMPLATE;

        GeminiRequest request = new GeminiRequest(prompt);

        String aiComment = getGeminiResponseResponseEntity(request, headers)
                .getBody()
                .candidates().get(0)
                .content().parts()
                .get(0).text()
                .replace("\n", "")
                .replaceAll("\\s{2,}", " ");

        return aiComment;
    }

    private ResponseEntity<GeminiResponse> getGeminiResponseResponseEntity(GeminiRequest request, HttpHeaders headers) {
        try {
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
        } catch (HttpServerErrorException.ServiceUnavailable e) {
            log.error("Gemini API가 과부하 상태입니다. (503 Service Unavailable)", e);
            throw new DiaryException(DiaryErrorCode.SERVICE_UNAVAILABLE);
        } catch (Exception e) {
            log.error("Gemini API 요청 중 오류가 발생했습니다.", e);
            throw new DiaryException(DiaryErrorCode.GEMINI_SERVICE_UNAVAILABLE);
        }
    }
}
