package com.teacher.qualification.repository.user;

import com.teacher.qualification.domain.user.AnswerHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerHistoryRepository extends JpaRepository<AnswerHistory, Long> {
    List<AnswerHistory> findByUserIdOrderByAnsweredAtDesc(Long userId);

    void deleteByQuestionId(Long questionId);
}