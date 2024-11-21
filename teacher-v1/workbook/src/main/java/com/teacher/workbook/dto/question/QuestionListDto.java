package com.teacher.workbook.dto.question;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class QuestionListDto {

    private Long question_id;

    private String nickname;
    private String title; // 문제 게시글 제목

    private Integer totalPeopleNum; // 사용자들이 문제를 시도한 수
    private Integer totalCorrectPeopleNum ; // 사용자들이 문제를 맞춘 수

    private LocalDateTime updatedAt;

    // 아래 생성자를 추가합니다.
    public QuestionListDto(Long question_id, String nickname, String title, Integer totalPeopleNum, Integer totalCorrectPeopleNum, LocalDateTime updatedAt) {
        this.question_id = question_id;
        this.nickname = nickname;
        this.title = title;
        this.totalPeopleNum = totalPeopleNum;
        this.totalCorrectPeopleNum = totalCorrectPeopleNum;
        this.updatedAt = updatedAt;
    }
}
