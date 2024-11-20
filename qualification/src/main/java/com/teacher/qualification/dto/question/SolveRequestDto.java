package com.teacher.qualification.dto.question;

import java.util.Set;

public record SolveRequestDto(
        Set<Integer> answers, // 선택한 Answer의 number들이 저장될거야
        String subjectiveAnswer // 주관식 답변
) {
}