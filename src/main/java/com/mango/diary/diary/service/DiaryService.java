package com.mango.diary.diary.service;

import com.mango.diary.auth.domain.User;
import com.mango.diary.auth.exception.MAuthErrorCode;
import com.mango.diary.auth.exception.MAuthException;
import com.mango.diary.auth.repository.UserRepository;

import com.mango.diary.common.enums.Emotion;
import com.mango.diary.diary.dto.*;

import com.mango.diary.diary.domain.AiComment;

import com.mango.diary.diary.exception.DiaryErrorCode;
import com.mango.diary.diary.exception.DiaryException;
import com.mango.diary.diary.repository.AiCommentRepository;
import com.mango.diary.diary.domain.Diary;
import com.mango.diary.diary.repository.DiaryRepository;
import com.mango.diary.statistics.entity.EmotionStatistics;
import com.mango.diary.statistics.repository.StatisticsRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;


@RequiredArgsConstructor
@Service
public class DiaryService {

    private final DiaryRepository diaryRepository;
    private final AiCommentRepository aiCommentRepository;
    private final UserRepository userRepository;
    private final StatisticsRepository statisticsRepository;

    @Transactional
    public DiaryIdDTO createDiary(DiaryRequest diaryRequest, Long userId) {
        if (diaryRepository.existsByUserIdAndDate(userId, diaryRequest.date())) {
            throw new DiaryException(DiaryErrorCode.DIARY_ENTRY_ALREADY_EXISTS);
        }

        if (diaryRequest.content().isEmpty()) {
            throw new DiaryException(DiaryErrorCode.INVALID_CONTENT);
        }

        if (diaryRequest.date().toString().isEmpty()) {
            throw new DiaryException(DiaryErrorCode.INVALID_DATE);
        }

        User user = userRepository.findById(userId).
                orElseThrow(() -> new MAuthException(MAuthErrorCode.USER_NOT_FOUND));

        Diary diary = Diary.builder()
                .content(diaryRequest.content())
                .date(diaryRequest.date())
                .emotion(diaryRequest.emotion())
                .user(user)
                .build();

        AiComment aiComment = AiComment.builder()
                .content(diaryRequest.aiComment())
                .diary(diary)
                .build();

        diaryRepository.save(diary);
        aiCommentRepository.save(aiComment);

        Optional<EmotionStatistics> emotionStatistics = statisticsRepository.findByUserIdAndYearMonth(userId, YearMonth.from(diary.getDate()));

        EmotionStatistics statistics;
        statistics = emotionStatistics.orElseGet(() -> EmotionStatistics.builder()
                .user(user)
                .yearMonth(YearMonth.from(diary.getDate()))
                .build());
        statistics.increaseEmotionCount(diary.getEmotion());
        statisticsRepository.save(statistics);

        return new DiaryIdDTO(diary.getId());
    }

    public DiaryDetailResponse getDiary(Long userId, Long diaryId) {
        Diary diary = diaryRepository.findById(diaryId).orElseThrow(() -> new DiaryException(DiaryErrorCode.DIARY_NOT_FOUND));

        if (!diary.getUser().getId().equals(userId)){
            throw new DiaryException(DiaryErrorCode.UNAUTHORIZED_ACCESS);
        }

        return new DiaryDetailResponse(
                diary.getId(),
                diary.getContent(),
                diary.getDate(),
                diary.getEmotion(),
                aiCommentRepository.findByDiaryId(diary.getId()).orElseThrow(() -> new DiaryException(DiaryErrorCode.AI_COMMENT_NOT_FOUND)).getContent()
        );
    }

    public Page<DiaryResponse> getAllDiaries(int page, int size, Long userId){
        Pageable pageable = PageRequest.of(page, size);
        Page<Diary> diaries = diaryRepository.findByUserIdOrderByDateDesc(userId, pageable);

        return diaries.map(diary -> new DiaryResponse(
                diary.getId(),
                diary.getContent(),
                diary.getDate(),
                diary.getEmotion()
        ));
    }

    public Page<DiaryResponse> getAllDiariesByEmotion(int page, int size, Emotion emotion,Long userId){
       Pageable pageable = PageRequest.of(page, size);
       Page<Diary> diaries = diaryRepository.findByUserIdAndEmotionOrderByDateDesc(userId, emotion, pageable);
       return diaries.map(diary -> new DiaryResponse(
               diary.getId(),
               diary.getContent(),
               diary.getDate(),
               diary.getEmotion()
       ));
    }

    public List<DiaryForCalenderResponse> getAllDiariesByMonth( YearMonth yearMonth , Long userId) {
        List<Diary> diaries = diaryRepository.findAllByDateBetweenAndUserId(yearMonth.atDay(1), yearMonth.atEndOfMonth(), userId);

        if (diaries.isEmpty()) {
            throw new DiaryException(DiaryErrorCode.LIST_IS_NULL);
        }

        return diaries.stream().map(diary -> new DiaryForCalenderResponse(
                diary.getId(),
                diary.getDate(),
                diary.getEmotion()
        )).toList();
    }

    @Transactional
    public void deleteDiary(Long diaryId, Long userId) {
        if (!diaryRepository.existsById(diaryId)) {
            throw new DiaryException(DiaryErrorCode.DIARY_NOT_FOUND);
        } else {
            Diary diary = diaryRepository.findById(diaryId).orElseThrow(() -> new DiaryException(DiaryErrorCode.DIARY_NOT_FOUND));
            if(!diary.getUser().getId().equals(userId)){
                throw new DiaryException(DiaryErrorCode.UNAUTHORIZED_ACCESS);
            }

            statisticsRepository.findByUserIdAndYearMonth(diary.getUser().getId(), YearMonth.from(diary.getDate()))
                    .ifPresent(statistics -> {
                        statistics.decreaseEmotionCount(diary.getEmotion());
                        statisticsRepository.save(statistics);
                    });
            diaryRepository.delete(diary);
        }
    }

    public Page<DiaryListDTO> searchDiary(String keyword, int page, int size, Long userId) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("date").descending());
        Page<Diary> diaryPage = diaryRepository.findByUserIdAndContentContainingIgnoreCase(userId, keyword, pageable);
        return diaryPage.map(diary -> new DiaryListDTO(
                diary.getId(),
                diary.getContent(),
                diary.getDate(),
                diary.getEmotion()));
    }
}
