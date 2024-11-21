package com.teacher.qualification.dto.user;

public record UserInfo(
        String nickname,
        String password,
        String phoneNumber,
        String email,
        String name
) {
}
