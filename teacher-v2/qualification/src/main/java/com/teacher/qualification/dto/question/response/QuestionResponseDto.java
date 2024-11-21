package com.teacher.qualification.dto.question.response;

import com.teacher.qualification.domain.question.QuestionType;

import java.time.LocalDateTime;
import java.util.List;

public record QuestionResponseDto(
        String nickname,
        String title,
        String content,
        QuestionType questionType,
        String image,
        LocalDateTime updatedAt,
        List<OptionResponseDto> options,
        boolean isPasteExam
) {

}
