package com.mango.diary.diary.repository;

import com.mango.diary.diary.domain.AiComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AiCommentRepository extends JpaRepository<AiComment, Long> {

    Optional<AiComment> findByDiaryId(Long id);
}
