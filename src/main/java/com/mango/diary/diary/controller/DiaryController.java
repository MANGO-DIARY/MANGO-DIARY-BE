package com.mango.diary.diary.controller;

import com.mango.diary.diary.service.DiaryService;
import com.mango.diary.diary.dto.DiaryRequest;
import com.mango.diary.diary.dto.DiaryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class DiaryController {

    private final DiaryService diaryService;

    @PostMapping("/diary")
    public ResponseEntity<DiaryResponse> saveDiary(@RequestBody DiaryRequest diaryRequest) {
        return ResponseEntity.ok(diaryService.createDiary(diaryRequest));
    }

    @GetMapping("/diary")
    public ResponseEntity<DiaryResponse> getDiary(@RequestParam Long id) {
        return ResponseEntity.ok(diaryService.readDiary(id));
    }

    @DeleteMapping("diary")
    public ResponseEntity<Void> deleteDiary(@RequestParam Long id) {
        diaryService.deleteDiary(id);
        return ResponseEntity.noContent().build();
    }
}
