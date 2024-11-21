package com.teacher.qualification.dto.question;

import java.time.LocalDateTime;

public record QuestionListDto(
        Long question_id,
        String nickname,
        String title, // 문제 게시글 제목
        Integer totalPeopleNum, // 사용자들이 문제를 시도한 수
        Integer totalCorrectPeopleNum, // 사용자들이 문제를 맞춘 수
        LocalDateTime updatedAt,
        Boolean isPastExam
) {
}