package com.teacher.qualification.dto.question.request;

import java.util.List;

public record QuestionCreateRequest(
        QuestionRequestDto questionDto,
        List<OptionRequestDto> optionDtos,
        AnswerRequestDto answerDto
) {
}