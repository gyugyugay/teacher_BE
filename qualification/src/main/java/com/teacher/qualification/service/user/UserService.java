package com.teacher.qualification.service.user;

import com.teacher.qualification.domain.user.AnswerHistory;
import com.teacher.qualification.domain.user.User;
import com.teacher.qualification.dto.user.UserInfo;
import com.teacher.qualification.dto.user.UserUpdateDto;
import com.teacher.qualification.repository.user.AnswerHistoryRepository;
import com.teacher.qualification.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AnswerHistoryRepository answerHistoryRepository;

    public User updateUser(UserUpdateDto userUpdateDto) {
        User user = findUser();
        user.update(userUpdateDto);
        return userRepository.save(user);
    }

    public boolean deleteUserById() {
        User user = findUser();
        // 사용자가 존재하는 경우, 삭제 수행
        userRepository.deleteById(user.getId());
        // 삭제 후, 해당 ID의 사용자가 정말로 삭제되었는지 다시 확인하여 결과 반환
        return !userRepository.existsById(user.getId());
    }

    public Map<String, Set<Long>> getUserQuestionHistory() {
        User user = findUser();
        List<AnswerHistory> histories = answerHistoryRepository.findByUserIdOrderByAnsweredAtDesc(user.getId());
        Map<Long, AnswerHistory> latestHistories = new HashMap<>();

        for (AnswerHistory history : histories) {
            latestHistories.putIfAbsent(history.getQuestion().getId(), history);
        }

        Set<Long> correctQuestionIds = new HashSet<>();
        Set<Long> incorrectQuestionIds = new HashSet<>();

        for (AnswerHistory history : latestHistories.values()) {
            if (history.isCorrect()) {
                correctQuestionIds.add(history.getQuestion().getId());
            } else {
                incorrectQuestionIds.add(history.getQuestion().getId());
            }
        }

        Map<String, Set<Long>> result = new HashMap<>();
        result.put("correct", correctQuestionIds);
        result.put("incorrect", incorrectQuestionIds);
        return result;
    }

    public boolean isLogin() {
        User user = findUser();
        if (user == null) {
            return false; // 사용자가 로그인하지 않은 경우
        }
        return userRepository.existsById(user.getId());
    }


    public UserInfo getUserInfo() {
        User user = findUser();
        return new UserInfo(user.getNickname(), user.getPassword(), user.getPhoneNumber(), user.getEmail(), user.getName());
    }

    public User findUser() {
        String email = (String) RequestContextHolder.getRequestAttributes().getAttribute("email", RequestAttributes.SCOPE_REQUEST);
        if (email == null) {
            return null;
        }
        return userRepository.findByEmail(email);
    }
}


