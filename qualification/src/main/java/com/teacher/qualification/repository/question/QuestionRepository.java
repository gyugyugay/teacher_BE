package com.teacher.qualification.repository.question;

import com.teacher.qualification.domain.question.Question;
import com.teacher.qualification.dto.question.QuestionListDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    @Query("SELECT new com.teacher.qualification.dto.question.QuestionListDto(q.id, q.user.nickname, q.title, q.totalPeopleNum, q.totalCorrectPeopleNum, q.updatedAt, q.isPastExam) FROM Question q")
    List<QuestionListDto> findAllQuestionsWithStats();
}
