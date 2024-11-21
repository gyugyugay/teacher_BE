package com.teacher.workbook.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateDto {
    private String nickname;
    private String password;
    private String phoneNumber;
    private String email;
    private String name;
}
