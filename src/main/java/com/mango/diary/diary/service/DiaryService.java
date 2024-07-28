package com.mango.diary.diary.service;

import com.mango.diary.auth.domain.User;
import com.mango.diary.auth.repository.UserRepository;
import com.mango.diary.common.enums.Emotion;
import com.mango.diary.diary.domain.DiaryStatus;
import com.mango.diary.diary.dto.AiEmotionResponse;
import com.mango.diary.diary.exception.DiaryErrorCode;
import com.mango.diary.diary.exception.DiaryException;
import com.mango.diary.diary.repository.AiCommentRepository;
import com.mango.diary.diary.domain.Diary;
import com.mango.diary.diary.dto.DiaryRequest;
import com.mango.diary.diary.dto.DiaryResponse;
import com.mango.diary.diary.repository.DiaryRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class DiaryService {

    private final DiaryRepository diaryRepository;
    private final AiCommentRepository aiCommentRepository;
    private final UserRepository userRepository;
    private final GeminiService geminiService;

    @Transactional
    public AiEmotionResponse createDiary(DiaryRequest diaryRequest, Long userId) {
        if (diaryRepository.existsByDate(diaryRequest.date())) {
            throw new DiaryException(DiaryErrorCode.DIARY_ENTRY_LIMIT_EXCEEDED);
        }

        if (diaryRequest.content().isEmpty()) {
            throw new DiaryException(DiaryErrorCode.INVALID_CONTENT);
        }

        if (diaryRequest.date().toString().isEmpty()) {
            throw new DiaryException(DiaryErrorCode.INVALID_DATE);
        }

        User user = userRepository.findById(userId).
                orElseThrow(() -> new DiaryException(DiaryErrorCode.USER_NOT_FOUND));

        Diary diary = Diary.builder()
                .content(diaryRequest.content())
                .date(diaryRequest.date())
                .user(user)
                .status(DiaryStatus.WRITING)
                .build();

        diaryRepository.save(diary);

        List<Emotion> emotions = geminiService.analyzeEmotion(diary.getContent());

        return new AiEmotionResponse(diary.getId(), emotions);
    }

    public DiaryResponse readDiary(Long id) {
        Diary diary = diaryRepository.findById(id).orElseThrow(() -> new DiaryException(DiaryErrorCode.DIARY_NOT_FOUND));
        return new DiaryResponse(
                diary.getId(),
                diary.getContent(),
                diary.getDate().toString(),
                diary.getEmotion(),
                aiCommentRepository.findById(diary.getId()).orElseThrow().getContent()
        );
    }

    @Transactional
    public boolean deleteDiary(Long diary_id) {
        if (!diaryRepository.existsById(diary_id)) {
            throw new DiaryException(DiaryErrorCode.DIARY_NOT_FOUND);
        } else {
            Diary diary = diaryRepository.findById(diary_id).orElseThrow(() -> new DiaryException(DiaryErrorCode.DIARY_NOT_FOUND));

            diaryRepository.delete(diary);
            return true;
        }
    }
}
