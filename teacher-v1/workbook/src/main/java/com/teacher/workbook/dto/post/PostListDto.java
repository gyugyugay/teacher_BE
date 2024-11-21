package com.teacher.workbook.dto.post;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostListDto {
    private Long postId;
    private String title;
    private String nickname;
    private Long userId;
    private LocalDateTime updatedAt;

    // 생성자, getter, setter 생략

    public PostListDto(Long postId, String title, String nickname, Long userId, LocalDateTime updatedAt) {
        this.postId = postId;
        this.title = title;
        this.nickname = nickname;
        this.userId = userId;
        this.updatedAt = updatedAt;
    }

    // getter, setter 생략
}

