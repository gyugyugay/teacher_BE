package com.teacher.workbook.controller.user;

import com.teacher.workbook.domain.user.User;
import com.teacher.workbook.dto.user.UserInfo;
import com.teacher.workbook.dto.user.UserUpdateDto;
import com.teacher.workbook.service.question.QuestionService;
import com.teacher.workbook.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

//@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    @Operation(summary = "개인정보 불러오기")
    public ResponseEntity<UserInfo> getUserById(@PathVariable("id") Long id) {
        UserInfo userInfo = userService.findUserById(id);
        if (userInfo != null) {
            // 사용자 정보를 성공적으로 찾았을 경우 HTTP 상태 코드 200(OK)와 함께 사용자 정보 반환
            return ResponseEntity.ok(userInfo);
        } else {
            // 사용자 정보를 찾지 못했을 경우 HTTP 상태 코드 404(NOT FOUND) 반환
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "개인정보 수정")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody UserUpdateDto userUpdateDto) {
        User updatedUser = userService.updateUser(id, userUpdateDto);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "사용자 삭제")
    public ResponseEntity<?> deleteUserById(@PathVariable("id") Long id) {
        boolean isDeleted = userService.deleteUserById(id);
        if (isDeleted) {
            // 삭제 성공: HTTP 상태 코드 200(OK) 반환
            return ResponseEntity.ok().body("사용자가 성공적으로 삭제되었습니다.");
        } else {
            // 삭제 실패 (예: 사용자를 찾을 수 없음): HTTP 상태 코드 404(NOT FOUND) 반환
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 ID의 사용자를 찾을 수 없습니다.");
        }
    }

    @GetMapping("/answers/{userId}")
    @Operation(summary = "사용자가 맞은 문제, 틀린 문제 반환")
    public ResponseEntity<?> getUserAnswers(@PathVariable Long userId) {
        Map<String, Set<Long>> result = userService.getUserQuestionHistory(userId);
        return ResponseEntity.ok(result);
    }

}
