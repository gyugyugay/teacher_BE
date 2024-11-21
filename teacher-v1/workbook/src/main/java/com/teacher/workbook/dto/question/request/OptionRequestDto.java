package com.teacher.workbook.dto.question.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OptionRequestDto {

    private Integer number; // 몇 번 옵션인지
    private String content; // 옵션 내용

}
