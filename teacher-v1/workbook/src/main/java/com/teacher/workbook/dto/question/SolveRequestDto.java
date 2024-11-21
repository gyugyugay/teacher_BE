package com.teacher.workbook.dto.question;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
public class SolveRequestDto {
    private Set<Integer> answers; // 선택한 Answer의 number들이 저장될거야
    private String subjectiveAnswer;  // 주관식 답변
}
