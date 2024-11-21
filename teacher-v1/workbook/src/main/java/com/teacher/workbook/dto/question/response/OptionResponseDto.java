package com.teacher.workbook.dto.question.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OptionResponseDto {
    private Long optionId;
    private Integer number;
    private String content;
}
