package com.teacher.workbook.service.auth;

import com.teacher.workbook.domain.user.User;
import com.teacher.workbook.dto.auth.SignupRequestDto;
import com.teacher.workbook.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    //private final PasswordEncoder passwordEncoder;

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

        // 비밀번호 암호화
        //String encodedPassword = passwordEncoder.encode(signupRequest.getPassword());

        // User 엔티티 생성 및 저장
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
    public Long login(String email, String password) {
        User user = userRepository.findByEmail(email);

        if (user != null && user.getPassword().equals(password)) {
            return user.getId();
        } else {
            return null; // 사용자가 존재하지 않거나 비밀번호가 일치하지 않는 경우 로그인 실패
        }
    }

    public String findUserEmailByNameAndPhoneNumber(String name, String phoneNumber) {
        return userRepository.findByNameAndPhoneNumber(name, phoneNumber)
                .map(User::getEmail) // User 엔티티에서 이메일 가져오기
                .orElse(null); // 사용자를 찾지 못한 경우 null 반환
    }

/*    public String resetUserPasswordByEmailAndName(String email, String name) {
        return userRepository.findByNameAndEmail(name, email)
                .map(user -> {
                    String newPassword = UUID.randomUUID().toString(); // 간단한 예시로 UUID 사용. 실제로는 더 안전한 방법 사용 권장
                    user.setPassword(newPassword); // 새 비밀번호 설정
                    userRepository.save(user); // 업데이트된 사용자 정보 저장
                    return newPassword;
                })
                .orElse(null); // 사용자를 찾지 못한 경우 null 반환
    }*/

    public String resetUserPasswordByEmailAndName(String email, String name) {
        return userRepository.findByNameAndEmail(name, email)
                .map(user -> {
                    String newPassword = generatePassword(5); // 5글자 영문+숫자 비밀번호 생성
                    user.setPassword(newPassword); // 새 비밀번호 설정
                    userRepository.save(user); // 업데이트된 사용자 정보 저장
                    return newPassword;
                })
                .orElse(null); // 사용자를 찾지 못한 경우 null 반환
    }

    private String generatePassword(int length) {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            if (random.nextBoolean()) {
                // 영문 (대문자 또는 소문자)
                char letter = (char) (random.nextBoolean() ? 'A' + random.nextInt(26) : 'a' + random.nextInt(26));
                sb.append(letter);
            } else {
                // 숫자
                char digit = (char) ('0' + random.nextInt(10));
                sb.append(digit);
            }
        }
        return sb.toString();
    }

}
