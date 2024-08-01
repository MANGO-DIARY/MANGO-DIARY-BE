package com.mango.diary.diary.repository;

import com.mango.diary.common.enums.Emotion;
import com.mango.diary.diary.domain.Diary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DiaryRepository extends JpaRepository<Diary, Long> {
    boolean existsByDate(LocalDate date);

    List<Diary> findAllByDateBetweenAndUserId(LocalDate firstDate, LocalDate endDate, Long userId);

    Page<Diary> findByUserIdOrderByDateDesc(Long userId, Pageable pageable);

    Page<Diary> findByUserIdAndEmotionOrderByDateDesc(Long userId, Emotion emotion, Pageable pageable);

    Page<Diary> findByUserIdAndContentContainingIgnoreCase(Long userId, String content, Pageable pageable);

    Optional<Diary> findFirstByUserIdOrderByDateDesc(Long userId);
}

