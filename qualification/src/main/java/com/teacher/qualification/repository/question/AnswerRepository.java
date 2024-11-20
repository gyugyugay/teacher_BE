package com.teacher.qualification.repository.question;

import com.teacher.qualification.domain.question.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
}