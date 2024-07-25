package com.mango.diary.diary.repository;

import com.mango.diary.diary.domain.AiComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AiCommentRepository extends JpaRepository<AiComment, Long> {
    void deleteAiCommentByDiaryId(Long id);
}
