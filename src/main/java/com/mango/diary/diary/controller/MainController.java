package com.mango.diary.diary.controller;

import com.mango.diary.auth.support.AuthUser;
import com.mango.diary.diary.dto.MainPageResponse;
import com.mango.diary.diary.service.MainService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class MainController {

    private final MainService mainService;

    @GetMapping("/main")
    public ResponseEntity<MainPageResponse> getMainPage(@Parameter(hidden = true) @AuthUser Long userId) {
        return ResponseEntity.ok().body(mainService.getMainPage(userId));
    }
}
