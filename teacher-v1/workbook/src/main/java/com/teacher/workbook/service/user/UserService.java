package com.teacher.workbook.service.user;

import com.teacher.workbook.domain.user.AnswerHistory;
import com.teacher.workbook.domain.user.User;
import com.teacher.workbook.dto.user.UserInfo;
import com.teacher.workbook.dto.user.UserUpdateDto;
import com.teacher.workbook.repository.user.AnswerHistoryRepository;
import com.teacher.workbook.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AnswerHistoryRepository answerHistoryRepository;

    public User updateUser(Long id, UserUpdateDto userUpdateDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        user.setNickname(userUpdateDto.getNickname());
        user.setEmail(userUpdateDto.getEmail());
        user.setPassword(userUpdateDto.getPassword());
        user.setName(userUpdateDto.getName());
        user.setPhoneNumber(userUpdateDto.getPhoneNumber());

        return userRepository.save(user);
    }

    public UserInfo findUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        UserInfo userInfo = new UserInfo();
        userInfo.setName(user.getName());
        userInfo.setEmail(user.getEmail());
        userInfo.setPhoneNumber(user.getPhoneNumber());
        userInfo.setPassword(user.getPassword());
        userInfo.setNickname(user.getNickname());
        return userInfo;
    }

    public boolean deleteUserById(Long id) {
        // 먼저, 해당 ID의 사용자가 존재하는지 확인
        if (!userRepository.existsById(id)) {
            // 해당 ID의 사용자가 존재하지 않는 경우, false 반환
            return false;
        }
        // 사용자가 존재하는 경우, 삭제 수행
        userRepository.deleteById(id);
        // 삭제 후, 해당 ID의 사용자가 정말로 삭제되었는지 다시 확인하여 결과 반환
        return !userRepository.existsById(id);
    }

    public Map<String, Set<Long>> getUserQuestionHistory(Long userId) {
        List<AnswerHistory> histories = answerHistoryRepository.findByUserIdOrderByAnsweredAtDesc(userId);
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

    public boolean isLogin(Long userId) {
        return userRepository.existsById(userId);
    }
}

