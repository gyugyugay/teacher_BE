package com.teacher.qualification.dto.user;

public record UserUpdateDto(
        String nickname,
        String password,
        String phoneNumber,
        String email,
        String name
) {
}