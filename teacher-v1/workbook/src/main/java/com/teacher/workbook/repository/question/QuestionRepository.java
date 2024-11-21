package com.teacher.workbook.repository.question;

import com.teacher.workbook.domain.question.Question;
import com.teacher.workbook.dto.question.QuestionListDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    @Query("SELECT new com.teacher.workbook.dto.question.QuestionListDto(q.id, q.user.nickname, q.title, q.totalPeopleNum, q.totalCorrectPeopleNum, q.updatedAt) FROM Question q")
    List<QuestionListDto> findAllQuestionsWithStats();
}