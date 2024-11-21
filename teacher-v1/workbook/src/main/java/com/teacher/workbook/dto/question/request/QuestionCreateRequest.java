package com.teacher.workbook.dto.question.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class QuestionCreateRequest {

    private QuestionRequestDto questionDto;
    private List<OptionRequestDto> optionDtos;
    private AnswerRequestDto answerDto;

}
