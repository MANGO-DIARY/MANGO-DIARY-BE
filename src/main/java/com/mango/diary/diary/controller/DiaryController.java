package com.mango.diary.diary.controller;

import com.mango.diary.auth.support.AuthUser;
import com.mango.diary.diary.dto.AiEmotionResponse;
import com.mango.diary.diary.exception.DiaryErrorCode;
import com.mango.diary.diary.exception.DiaryException;
import com.mango.diary.diary.service.DiaryService;
import com.mango.diary.diary.dto.DiaryRequest;
import com.mango.diary.diary.dto.DiaryResponse;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class DiaryController {

    private final DiaryService diaryService;

    @PostMapping("/diary")
    public ResponseEntity<?> saveDiary(@RequestBody DiaryRequest diaryRequest,
                                       @Parameter(hidden = true) @AuthUser Long userId) {
        diaryService.createDiary(diaryRequest, userId);
        return new ResponseEntity<>("일기가 작성되었습니다.", HttpStatus.CREATED);
    }

    @GetMapping("/diary")
    public ResponseEntity<DiaryResponse> getDiary(@RequestParam Long id) {
        return ResponseEntity.ok(diaryService.readDiary(id));
    }

    @DeleteMapping("/diary")
    public ResponseEntity<String> deleteDiary(@RequestParam Long diary_id) {
        if (diaryService.deleteDiary(diary_id)) {
            return new ResponseEntity<>("Diary deleted", HttpStatus.OK);
        } else {
            throw new DiaryException(DiaryErrorCode.DIARY_NOT_FOUND);
        }
    }
}
