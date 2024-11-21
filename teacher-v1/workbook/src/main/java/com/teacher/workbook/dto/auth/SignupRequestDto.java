package com.teacher.workbook.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequestDto {
    private String nickname;
    private String password;
    private String phoneNumber;
    private String email;
    private String name;
}