package com.mango.diary.diary.controller;

import com.mango.diary.auth.support.AuthUser;
import com.mango.diary.diary.service.DiaryService;
import com.mango.diary.diary.dto.DiaryRequest;
import com.mango.diary.diary.dto.DiaryResponse;
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
    public ResponseEntity<DiaryResponse> saveDiary(@RequestBody DiaryRequest diaryRequest, @AuthUser Long userId) {
        return ResponseEntity.ok(diaryService.createDiary(diaryRequest, userId));
    }

    @GetMapping("/diary")
    public ResponseEntity<DiaryResponse> getDiary(@RequestParam Long id) {
        return ResponseEntity.ok(diaryService.readDiary(id));
    }

    @DeleteMapping("/diary")
    public ResponseEntity<String> deleteDiary(@RequestParam Long diary_id) {
        return diaryService.deleteDiary(diary_id) ? new ResponseEntity<>("Diary deleted", HttpStatus.OK) : new ResponseEntity<>("Diary not found", HttpStatus.NOT_FOUND);
    }
}
