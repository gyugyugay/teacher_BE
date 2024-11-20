package com.teacher.qualification.dto.question.response;

public record AnswerResponseDto(
        String answers, // 정답. 복수 선택 가능하므로 "1,2"와 같은 형태로 저장
        String subjectiveAnswer, // 주관식 정답
        String image, // 해설 이미지
        String commentary // 해설

) {
}
