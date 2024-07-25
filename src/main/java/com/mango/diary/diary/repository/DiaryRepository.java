package com.mango.diary.diary.repository;

import com.mango.diary.diary.domain.Diary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface DiaryRepository extends JpaRepository<Diary, Long> {
    boolean existsByDate(LocalDate date);
}

