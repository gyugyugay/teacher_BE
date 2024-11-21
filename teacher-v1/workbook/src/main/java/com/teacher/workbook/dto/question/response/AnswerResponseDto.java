package com.teacher.workbook.dto.question.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AnswerResponseDto {
    private String answers; // 정답. 복수 선택 가능하므로 "1,2"와 같은 형태로 저장
    private String subjectiveAnswer; // 주관식 정답
    private String image; // 해설 이미지
    private String commentary; // 해설

    public AnswerResponseDto(String answers, String subjectiveAnswer, String image, String commentary) {
        this.answers = answers;
        this.subjectiveAnswer = subjectiveAnswer;
        this.image = image;
        this.commentary = commentary;
    }
}
