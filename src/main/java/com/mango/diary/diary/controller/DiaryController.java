package com.mango.diary.diary.controller;

import com.mango.diary.auth.support.AuthUser;

import com.mango.diary.diary.dto.DiaryListDTO;
import com.mango.diary.diary.exception.DiaryErrorCode;
import com.mango.diary.diary.exception.DiaryException;
import com.mango.diary.diary.service.DiaryService;
import com.mango.diary.diary.dto.DiaryRequest;
import com.mango.diary.diary.dto.DiaryResponse;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<DiaryDetailResponse> readDiary(@RequestParam Long diaryId) {
        return ResponseEntity.ok(diaryService.getDiary(diaryId));
    }

    @GetMapping("/diary/all")
    public ResponseEntity<Page<DiaryResponse>> readAllDiaries( @RequestParam int page, @RequestParam int size,
            @Parameter(hidden = true) @AuthUser Long userId) {
        return ResponseEntity.ok(diaryService.getAllDiaries(page, size, userId));
    }

    @GetMapping("/diary/all/emotion")
    public ResponseEntity<Page<DiaryResponse>> readAllDiariesByEmotion( @RequestParam int page, @RequestParam int size,
            @RequestParam Emotion emotion, @Parameter(hidden = true) @AuthUser Long userId) {
        return ResponseEntity.ok(diaryService.getAllDiariesByEmotion(page, size, emotion, userId));
    }

    @GetMapping("/diary/all/month")
    public ResponseEntity<List<DiaryForCalenderResponse>> readAllDiariesByMonth(@RequestParam("Year") Long year, @RequestParam("Month") Long month,  @Parameter(hidden = true) @AuthUser Long userId) {
        return ResponseEntity.ok(diaryService.getAllDiariesByMonth(year, month, userId));
    }

    @DeleteMapping("/diary")
    public ResponseEntity<String> deleteDiary(@RequestParam Long diary_id) {
        if (diaryService.deleteDiary(diary_id)) {
            return new ResponseEntity<>("Diary deleted", HttpStatus.OK);
        } else {
            throw new DiaryException(DiaryErrorCode.DIARY_NOT_FOUND);
        }
    }

    @GetMapping("/diary/search")
    public ResponseEntity<Page<DiaryListDTO>> searchDiary(@RequestParam String keyword,
                                                          @RequestParam int page,
                                                          @RequestParam int size,
                                                          @Parameter(hidden = true) @AuthUser Long userId){
        return ResponseEntity.ok(diaryService.searchDiary(keyword, page, size, userId));
    }
}
