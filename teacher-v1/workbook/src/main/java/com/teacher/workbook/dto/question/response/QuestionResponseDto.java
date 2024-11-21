package com.teacher.workbook.dto.question.response;

import com.teacher.workbook.domain.question.QuestionType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class QuestionResponseDto {
    private String nickname;
    private String title;
    private String content;
    private QuestionType questionType;
    private String image;
    private LocalDateTime updatedAt;
    private List<OptionResponseDto> options;
}
