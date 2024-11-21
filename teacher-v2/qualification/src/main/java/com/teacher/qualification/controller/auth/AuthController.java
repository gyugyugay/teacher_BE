package com.teacher.qualification.controller.auth;

import com.teacher.qualification.dto.auth.*;
import com.teacher.qualification.service.auth.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//@CrossOrigin(origins = "http://localhost:3001")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private final AuthService authService;

    @PostMapping("/join")
    @Operation(summary = "사용자 회원가입")
    public ResponseEntity<Void> signup(@RequestBody SignupRequestDto signupRequest) {
        authService.signup(signupRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    @Operation(summary = "사용자 로그인")
    public ResponseEntity<Token> login(@RequestBody LoginDto loginDto) {
        Token token = authService.login(loginDto.getEmail(), loginDto.getPassword());
        if (token != null) {
            return ResponseEntity.ok(token);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new Token("로그인 실패: 이메일 또는 비밀번호가 잘못되었습니다."));
        }
    }

    @PostMapping("/findEmail")
    @Operation(summary = "사용자 아이디 찾기")
    public ResponseEntity<Email> findUserId(@RequestBody FindEmailDto request) {
        Email email = new Email(authService.findUserEmailByNameAndPhoneNumber(request.getName(), request.getPhoneNumber()));
        if (email.email() != null) {
            return ResponseEntity.ok(email);
        } else {
            return ResponseEntity.status(401).body(new Email("사용자 정보가 일치하지 않습니다."));
        }
    }

    @PostMapping("/resetPassword")
    @Operation(summary = "사용자 비밀번호 재설정")
    public ResponseEntity<Void> resetPassword(@RequestBody ResetPasswordDto request) {
        String password = authService.createNewPassword(request.getEmail(), request.getName());
        if (password != null) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(401).build();
        }
    }

    @PostMapping("/check/phoneNumber")
    @Operation(summary = "전화번호 중복체크 - 중복이면 true, 아니면 false")
    public ResponseEntity<CheckDuplicate> checkDuplicatePhone(@RequestBody PhoneNumber phoneNumber) {
        return ResponseEntity.ok(authService.checkDuplicatePhoneNumber(phoneNumber));
    }

    @PostMapping("/check/email")
    @Operation(summary = "이메일 중복체크 - 중복이면 true, 아니면 false")
    public ResponseEntity<CheckDuplicate> checkDuplicateEmail(@RequestBody Email email) {
        return ResponseEntity.ok(authService.checkDuplicateEmail(email));
    }

    @PostMapping("/check/nickname")
    @Operation(summary = "닉네임 중복체크 - 중복이면 true, 아니면 false")
    public ResponseEntity<CheckDuplicate> checkDuplicateNickname(@RequestBody Nickname nickname) {
        return ResponseEntity.ok(authService.checkDuplicateNickname(nickname));
    }
}

