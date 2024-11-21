package com.teacher.workbook.repository.question;

import com.teacher.workbook.domain.question.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
}
