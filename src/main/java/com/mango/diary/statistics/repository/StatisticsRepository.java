package com.mango.diary.statistics.repository;

import com.mango.diary.statistics.entity.EmotionStatistics;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.YearMonth;
import java.util.Optional;

public interface StatisticsRepository extends JpaRepository<EmotionStatistics, Long> {

    Optional<EmotionStatistics> findByUserIdAndYearMonth(Long userId, YearMonth yearMonth);
}
