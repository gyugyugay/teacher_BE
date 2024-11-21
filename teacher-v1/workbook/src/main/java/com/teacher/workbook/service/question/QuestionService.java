package com.teacher.workbook.service.question;

import com.teacher.workbook.domain.question.Answer;
import com.teacher.workbook.domain.question.Choice;
import com.teacher.workbook.domain.question.Question;
import com.teacher.workbook.domain.user.AnswerHistory;
import com.teacher.workbook.domain.user.User;
import com.teacher.workbook.dto.question.SolveRequestDto;
import com.teacher.workbook.dto.question.request.OptionRequestDto;
import com.teacher.workbook.dto.question.request.QuestionCreateRequest;
import com.teacher.workbook.dto.question.request.QuestionRequestDto;
import com.teacher.workbook.dto.question.request.AnswerRequestDto;
import com.teacher.workbook.dto.question.QuestionListDto;
import com.teacher.workbook.dto.question.response.AnswerResponseDto;
import com.teacher.workbook.dto.question.response.OptionResponseDto;
import com.teacher.workbook.dto.question.response.QuestionResponseDto;
import com.teacher.workbook.exception.QuestionNotFoundException;
import com.teacher.workbook.exception.ResourceNotFoundException;
import com.teacher.workbook.exception.UnauthorizedException;
import com.teacher.workbook.repository.question.AnswerRepository;
import com.teacher.workbook.repository.question.OptionRepository;
import com.teacher.workbook.repository.question.QuestionRepository;
import com.teacher.workbook.repository.user.AnswerHistoryRepository;
import com.teacher.workbook.repository.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private OptionRepository optionRepository;
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private UserRepository userRepository; // 사용자 정보를 조회하기 위해
    @Autowired
    private AnswerHistoryRepository answerHistoryRepository;
    

    public List<QuestionListDto> getAllQuestionsList() {
        return questionRepository.findAllQuestionsWithStats();
    }

    @Transactional
    public Question createQuestion(Long userId, QuestionRequestDto questionDto, List<OptionRequestDto> optionDtos, AnswerRequestDto answerDto) {
        // 사용자 정보 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + userId));

        // Question 엔티티 생성 및 저장
        Question question = new Question();
        question.setUser(user);
        question.setTitle(questionDto.getTitle());
        question.setContent(questionDto.getContent());
        question.setQuestionType(questionDto.getQuestionType());
        question.setImage(questionDto.getImage());
        question.setIsPastExam(questionDto.isPastExam());
        questionRepository.save(question);

        List<Choice> choices = new ArrayList<>();
        // Option 엔티티 생성 및 저장
        for (OptionRequestDto optionDto : optionDtos) {
            Choice choice = new Choice();
            choice.setQuestion(question);
            choice.setNumber(optionDto.getNumber());
            choice.setContent(optionDto.getContent());
            choices.add(choice);
            optionRepository.save(choice);
        }

        // Answer 엔티티 생성 및 저장
        Answer answer = new Answer();
        answer.setQuestion(question);
        answer.setAnswers(answerDto.getAnswers());
        answer.setSubjectiveAnswer(answerDto.getSubjectiveAnswer());
        answer.setImage(answerDto.getImage());
        answer.setCommentary(answerDto.getCommentary());
        answerRepository.save(answer);

        return question;
    }
    @Transactional
    public void deleteQuestion(Long questionId, Long userId) throws IllegalAccessException {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new EntityNotFoundException("이 id를 가진 문제가 없습니다. " + questionId));

        if (!question.getUser().getId().equals(userId)) {
            throw new IllegalAccessException("문제를 지울 권한이 없습니다.");
        }
        answerHistoryRepository.deleteByQuestionId(questionId);
        questionRepository.delete(question);
    }

    public QuestionResponseDto getQuestionById(Long questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new QuestionNotFoundException("문제 게시글을 찾을 수 없습니다. ID: " + questionId));

        QuestionResponseDto questionResponseDto = new QuestionResponseDto();
        questionResponseDto.setNickname(question.getUser().getNickname());
        questionResponseDto.setTitle(question.getTitle());
        questionResponseDto.setContent(question.getContent());
        questionResponseDto.setQuestionType(question.getQuestionType());
        questionResponseDto.setImage(question.getImage());
        questionResponseDto.setUpdatedAt(question.getUpdatedAt());

        List<OptionResponseDto> options = question.getChoices().stream().map(choice -> {
            OptionResponseDto optionResponseDto = new OptionResponseDto();
            optionResponseDto.setOptionId(choice.getId());
            optionResponseDto.setNumber(choice.getNumber());
            optionResponseDto.setContent(choice.getContent());
            return optionResponseDto;
        }).collect(Collectors.toList());

        questionResponseDto.setOptions(options);

        return questionResponseDto;
    }

    @Transactional
    public void updateQuestion(Long questionId, Long userId, QuestionCreateRequest request) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new EntityNotFoundException("해당 id를 가진 문제가 없습니다. " + questionId));

        // userId가 일치하는지 확인
        if (!question.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("수정권한이 없습니다.");
        }

        // Question 객체 업데이트
        question.setTitle(request.getQuestionDto().getTitle());
        question.setContent(request.getQuestionDto().getContent());
        question.setQuestionType(request.getQuestionDto().getQuestionType());
        question.setIsPastExam(request.getQuestionDto().isPastExam());
        question.setImage(request.getQuestionDto().getImage());

        // 기존 옵션 삭제 로직은 위의 설정에 따라 자동으로 처리될 수 있으므로 생략됨
        question.getChoices().clear(); // 연관된 모든 Choice 엔티티를 컬렉션에서 제거

        // 새 옵션 추가
        for (OptionRequestDto optionDto : request.getOptionDtos()) {
            Choice choice = new Choice();
            choice.setNumber(optionDto.getNumber());
            choice.setContent(optionDto.getContent());
            choice.setQuestion(question);
            question.getChoices().add(choice); // 새 Choice 엔티티를 컬렉션에 추가
        }

        // Answer 업데이트
        Answer answer = question.getAnswer();
        if (answer == null) {
            answer = new Answer();
            answer.setQuestion(question);
            question.setAnswer(answer);
        }
        answer.setAnswers(request.getAnswerDto().getAnswers());
        answer.setSubjectiveAnswer(request.getAnswerDto().getSubjectiveAnswer());
        answer.setImage(request.getAnswerDto().getImage());
        answer.setCommentary(request.getAnswerDto().getCommentary());

        // 저장
        questionRepository.save(question);
    }

    public AnswerResponseDto getAnswerByQuestionId(Long questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new EntityNotFoundException("해당 id를 가진 문제가 없습니다. " + questionId));

        Answer answer = question.getAnswer();
        if (answer == null) {
            throw new EntityNotFoundException("해당 문제에 대한 답이 없습니다. " + questionId);
        }

        return new AnswerResponseDto(answer.getAnswers(), answer.getSubjectiveAnswer(),
                answer.getImage(), answer.getCommentary());
    }

    @Transactional
    public boolean solveQuestion(Long questionId, Long userId, SolveRequestDto solveRequestDto) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Answer answer = question.getAnswer();

        // 정답 체크 로직
        boolean isCorrect = checkCorrectness(answer, solveRequestDto);

        // 사용자들이 문제를 시도한 수 증가
        question.setTotalPeopleNum(question.getTotalPeopleNum() + 1);

        // 맞춘 경우 사용자들이 문제를 맞춘 수 증가
        if (isCorrect) {
            question.setTotalCorrectPeopleNum(question.getTotalCorrectPeopleNum() + 1);
        }

        // AnswerHistory 저장
        AnswerHistory answerHistory = new AnswerHistory();
        answerHistory.setUser(user);
        answerHistory.setQuestion(question);
        answerHistory.setAnswers(solveRequestDto.getAnswers());
        answerHistory.setSubjectiveAnswer(solveRequestDto.getSubjectiveAnswer());
        answerHistory.setCorrect(isCorrect);

        answerHistoryRepository.save(answerHistory);

        return isCorrect;
    }

    private boolean checkCorrectness(Answer answer, SolveRequestDto solveRequestDto) {
        if (answer.getAnswers() != null) {
            Set<Integer> correctAnswers = Arrays.stream(answer.getAnswers().split(","))
                    .map(Integer::parseInt)
                    .collect(Collectors.toSet());
            return correctAnswers.equals(solveRequestDto.getAnswers());
        } else {
            return answer.getSubjectiveAnswer().equals(solveRequestDto.getSubjectiveAnswer());
        }
    }

}
