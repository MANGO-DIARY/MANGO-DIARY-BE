package com.mango.diary.statistics.controller;

import com.mango.diary.auth.support.AuthUser;
import com.mango.diary.statistics.dto.StatisticsResponse;
import com.mango.diary.statistics.service.StatisticsService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.YearMonth;

@RestController
@RequestMapping("/api/v1/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping()
    public ResponseEntity<StatisticsResponse> getStatistics(@Parameter(hidden = true) @AuthUser Long userId,
                                                            @Parameter(description = "yyyy-MM 형태로 입력", example = "2024-08")
                                                            @RequestParam YearMonth yearMonth) {
        return ResponseEntity.ok(statisticsService.getStatistics(userId, yearMonth));
    }
}
