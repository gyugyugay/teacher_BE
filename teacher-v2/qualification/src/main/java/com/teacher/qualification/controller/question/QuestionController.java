package com.teacher.qualification.controller.question;

import com.teacher.qualification.domain.question.Question;
import com.teacher.qualification.dto.question.QuestionListDto;
import com.teacher.qualification.dto.question.SolveRequestDto;
import com.teacher.qualification.dto.question.request.QuestionCreateRequest;
import com.teacher.qualification.dto.question.response.AnswerResponseDto;
import com.teacher.qualification.dto.question.response.QuestionResponseDto;
import com.teacher.qualification.exception.QuestionNotFoundException;
import com.teacher.qualification.service.question.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/questions")
public class QuestionController {
    private final QuestionService questionService;

    @Autowired
    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping
    @Operation(summary = "문제 게시글 다 가져오기")
    public ResponseEntity<?> getAllQuestions() {
        List<QuestionListDto> questions = questionService.getAllQuestionsList();

        // 게시글 리스트가 비어있는지 확인
        if (questions.isEmpty()) {
            // 비어있다면 적절한 메시지와 함께 NOT_FOUND 상태 코드를 반환
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("게시글이 없습니다.");
        }

        // 게시글이 있다면 리스트를 반환
        return ResponseEntity.ok(questions);
    }

    @PostMapping
    @Operation(summary = "문제 게시글 작성")
    public ResponseEntity<Void> createQuestion(@RequestBody QuestionCreateRequest request) {
        try {
            questionService.createQuestion(request);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{questionId}")
    @Operation(summary = "문제 불러오기")
    public ResponseEntity<QuestionResponseDto> getQuestion(@PathVariable Long questionId) {
        QuestionResponseDto questionResponseDto = questionService.getQuestionById(questionId);
        return ResponseEntity.ok(questionResponseDto);
    }

    @GetMapping("/answer/{question_id}")
    @Operation(summary = "정답 불러오기")
    public ResponseEntity<AnswerResponseDto> getAnswer(@PathVariable("question_id") Long questionId) {
        AnswerResponseDto answer = questionService.getAnswerByQuestionId(questionId);
        return ResponseEntity.ok(answer);
    }

    @PutMapping("/{questionId}")
    @Operation(summary = "문제 수정하기")
    public ResponseEntity<Void> updateQuestion(
            @PathVariable Long questionId,
            @RequestBody QuestionCreateRequest request) {

        questionService.updateQuestion(questionId, request);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{question_id}")
    @Operation(summary = "문제 삭제")
    public ResponseEntity<Object> deleteQuestion(@PathVariable Long question_id) {
        try {
            questionService.deleteQuestion(question_id);
            return ResponseEntity.ok().build();
        } catch (IllegalAccessException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/{question_id}")
    @Operation(summary = "문제 풀기")
    public ResponseEntity<Boolean> solveQuestion(@PathVariable Long question_id,
                                                 @RequestBody SolveRequestDto solveRequestDto) {
        boolean isCorrect = questionService.solveQuestion(question_id, solveRequestDto);

        return ResponseEntity.ok(isCorrect);
    }

    @ExceptionHandler(QuestionNotFoundException.class)
    public ResponseEntity<String> handleQuestionNotFoundException(QuestionNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

}

