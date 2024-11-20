package com.teacher.qualification.dto.question.request;

import com.teacher.qualification.domain.question.QuestionType;

public record QuestionRequestDto(
        String title, // 문제 게시글 제목
        String content, // 문제 내용
        QuestionType questionType, //  Choices4,Choices5
        boolean isPastExam, // 기출 유무
        String image // 해설 이미지 경로 저장
) {


}
