package com.teacher.qualification.dto.post;

public record PostCreateDto(
        String title,
        String content,
        String imageUrl
) {
}
