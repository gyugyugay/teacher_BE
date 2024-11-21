package com.teacher.workbook.dto.question.request;

import com.teacher.workbook.domain.question.QuestionType;
import lombok.Getter;

@Getter
public class QuestionRequestDto {

    private String title; // 문제 게시글 제목
    private String content; // 문제 내용
    private QuestionType questionType; //  Choices4,Choices5
    private boolean isPastExam; // 기출 유무
    private String image; // 해설 이미지 경로 저장

}
