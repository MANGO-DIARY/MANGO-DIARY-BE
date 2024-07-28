package com.mango.diary.diary.repository;

import com.mango.diary.diary.domain.Diary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface DiaryRepository extends JpaRepository<Diary, Long> {
    boolean existsByDate(LocalDate date);
    Page<Diary> findByUserIdAndContentContainingIgnoreCase(Long userId, String content, Pageable pageable);
}

