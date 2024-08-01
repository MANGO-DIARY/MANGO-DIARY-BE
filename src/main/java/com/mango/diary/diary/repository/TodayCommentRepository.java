package com.mango.diary.diary.repository;

import com.mango.diary.diary.domain.TodayComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface TodayCommentRepository extends JpaRepository<TodayComment, Long> {
    Boolean existsByDate(LocalDate date);
    Optional<TodayComment> findByDate(LocalDate date);
}
