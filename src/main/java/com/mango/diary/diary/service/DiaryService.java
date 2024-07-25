package com.mango.diary.diary.service;

import com.mango.diary.common.enums.Emotion;
import com.mango.diary.diary.exception.DiaryErrorCode;
import com.mango.diary.diary.exception.DiaryException;
import com.mango.diary.diary.repository.AiCommentRepository;
import com.mango.diary.diary.domain.AiComment;
import com.mango.diary.diary.domain.Diary;
import com.mango.diary.diary.dto.DiaryRequest;
import com.mango.diary.diary.dto.DiaryResponse;
import com.mango.diary.diary.repository.DiaryRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class DiaryService {

    private final DiaryRepository diaryRepository;
    private final AiCommentRepository aiCommentRepository;

    @Transactional
    public DiaryResponse createDiary(DiaryRequest diaryRequest) {
        if(diaryRepository.existsByDate(diaryRequest.date().toLocalDate())){
            throw new DiaryException(DiaryErrorCode.DIARY_ENTRY_LIMIT_EXCEEDED);
        }

        if(diaryRequest.content().isEmpty()){
            throw new DiaryException(DiaryErrorCode.INVALID_CONTENT);
        }

        if(diaryRequest.date().toString().isEmpty()){
            throw new DiaryException(DiaryErrorCode.INVALID_DATE);
        }

        if(diaryRequest.emotion().toString().isEmpty()){
            throw new DiaryException(DiaryErrorCode.INVALID_EMOTION);
        }

        if(!EnumUtils.isValidEnum(Emotion.class, diaryRequest.emotion().name())){
            throw new DiaryException(DiaryErrorCode.INVALID_EMOTION_TYPE);
        }


        Diary diary = Diary.builder()
                .content(diaryRequest.content())
                .date(diaryRequest.date().toLocalDate())
                .emotion(diaryRequest.emotion())
                .user(diaryRequest.user())
                .build();

       AiComment aiComment = AiComment.builder()
               .content(diaryRequest.aiComment())
               .diary(diary)
               .build();

       diaryRepository.save(diary);
       aiCommentRepository.save(aiComment);

       String aiCommentContent = aiCommentRepository.findById(diary.getId())
                .map(AiComment::getContent)
                .orElseThrow(() -> new DiaryException(DiaryErrorCode.AI_COMMENT_NOT_FOUND));


        return new DiaryResponse(
               diary.getId(),
               diary.getContent(),
               diary.getDate().toString(),
               diary.getEmotion().changeString(),
               aiCommentContent,
               diary.getUser()
       );
    }

    public DiaryResponse readDiary(Long id) {
        Diary diary = diaryRepository.findById(id).orElseThrow(() -> new DiaryException(DiaryErrorCode.DIARY_NOT_FOUND));
        return new DiaryResponse(
                diary.getId(),
                diary.getContent(),
                diary.getDate().toString(),
                diary.getEmotion().changeString(),
                aiCommentRepository.findById(diary.getId()).orElseThrow().getContent(),
                diary.getUser()
        );
    }

    @Transactional
    public void deleteDiary(Long id) {
        if (!diaryRepository.existsById(id)) {
            throw new DiaryException(DiaryErrorCode.DIARY_NOT_FOUND);
        }
        aiCommentRepository.deleteAiCommentByDiaryId(id);
        diaryRepository.deleteById(id);
    }
}
