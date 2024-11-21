package com.teacher.qualification.service.auth;

import com.teacher.qualification.domain.user.User;
import com.teacher.qualification.dto.auth.*;
import com.teacher.qualification.repository.user.UserRepository;
import com.teacher.qualification.service.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final JavaMailSender emailSender;

    private static final int TEMP_PASSWORD_LENGTH = 8;

    // 회원가입
    public User signup(SignupRequestDto signupRequest) {
        // 중복 닉네임 및 이메일 체크
        if (userRepository.existsByNickname(signupRequest.getNickname())) {
            throw new IllegalArgumentException("Nickname already exists");
        }
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        if (userRepository.existsByPhoneNumber(signupRequest.getPhoneNumber())){
            throw new IllegalArgumentException("Phone number already exists");
        }

        User user = User.builder()
                .nickname(signupRequest.getNickname())
                //.password(encodedPassword)
                .password(signupRequest.getPassword())
                .phoneNumber(signupRequest.getPhoneNumber())
                .email(signupRequest.getEmail())
                .name(signupRequest.getName())
                .build();
        return userRepository.save(user);
    }

    // 로그인
    public Token login(String email, String password) {
        User user = userRepository.findByEmail(email);

        if (user != null && user.getPassword().equals(password)) {
            return new Token(jwtService.generateToken(email));
        } else {
            return null; // 사용자가 존재하지 않거나 비밀번호가 일치하지 않는 경우 로그인 실패
        }
    }

    public String findUserEmailByNameAndPhoneNumber(String name, String phoneNumber) {
        User user = userRepository.findByPhoneNumber(phoneNumber);
        if (user == null) {
            return null; // 사용자 정보를 찾지 못한 경우 null을 반환하거나 적절한 예외를 던질 수 있습니다.
        }
        if (user.getName().equals(name)) {
            return user.getEmail();
        }
        return null;
    }

    @Transactional
    public String createNewPassword(String email, String name) {
        User user = userRepository.findByEmail(email);
        String tempPassword = generateRandomPassword();
        user.updatePassword(tempPassword);
        sendEmail(user.getEmail(), tempPassword);
        return tempPassword;
    }

    private String generateRandomPassword() {
        SecureRandom random = new SecureRandom();
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder password = new StringBuilder(TEMP_PASSWORD_LENGTH);
        for (int i = 0; i < TEMP_PASSWORD_LENGTH; i++) {
            password.append(characters.charAt(random.nextInt(characters.length())));
        }
        return password.toString();
    }

    @Async
    public void sendEmail(String to, String temporaryPassword) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("[꿈꾸는 임용] 비밀번호 재발급 안내");
        message.setText(
                "새로 생성된 비밀번호 입니다: " + temporaryPassword + "\n\n해당 비밀번호로 로그인 후 반드시 비밀번호를 변경해 주시기 바랍니다.");
        emailSender.send(message);
    }

    public CheckDuplicate checkDuplicatePhoneNumber(PhoneNumber phoneNumber) {
        boolean isDuplicate = userRepository.existsByPhoneNumber(phoneNumber.phoneNumber());
        return new CheckDuplicate(isDuplicate);
    }

    public CheckDuplicate checkDuplicateEmail(Email email) {
        boolean isDuplicate = userRepository.existsByEmail(email.email());
        return new CheckDuplicate(isDuplicate);
    }

    public CheckDuplicate checkDuplicateNickname(Nickname nickname) {
        boolean isDuplicate = userRepository.existsByNickname(nickname.nickname());
        return new CheckDuplicate(isDuplicate);
    }
}

