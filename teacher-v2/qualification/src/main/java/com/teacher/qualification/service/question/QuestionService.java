package com.teacher.qualification.service.question;

import com.teacher.qualification.domain.question.Answer;
import com.teacher.qualification.domain.question.Choice;
import com.teacher.qualification.domain.question.Question;
import com.teacher.qualification.domain.user.AnswerHistory;
import com.teacher.qualification.domain.user.User;
import com.teacher.qualification.dto.question.QuestionListDto;
import com.teacher.qualification.dto.question.SolveRequestDto;
import com.teacher.qualification.dto.question.request.AnswerRequestDto;
import com.teacher.qualification.dto.question.request.OptionRequestDto;
import com.teacher.qualification.dto.question.request.QuestionCreateRequest;
import com.teacher.qualification.dto.question.request.QuestionRequestDto;
import com.teacher.qualification.dto.question.response.AnswerResponseDto;
import com.teacher.qualification.dto.question.response.OptionResponseDto;
import com.teacher.qualification.dto.question.response.QuestionResponseDto;
import com.teacher.qualification.exception.QuestionNotFoundException;
import com.teacher.qualification.exception.ResourceNotFoundException;
import com.teacher.qualification.exception.UnauthorizedException;
import com.teacher.qualification.repository.question.AnswerRepository;
import com.teacher.qualification.repository.question.OptionRepository;
import com.teacher.qualification.repository.question.QuestionRepository;
import com.teacher.qualification.repository.user.AnswerHistoryRepository;
import com.teacher.qualification.repository.user.UserRepository;
import com.teacher.qualification.service.user.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
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
    private AnswerHistoryRepository answerHistoryRepository;
    @Autowired
    private UserService userService;


    public List<QuestionListDto> getAllQuestionsList() {
        return questionRepository.findAllQuestionsWithStats();
    }

    @Transactional
    public void createQuestion(QuestionCreateRequest request) {
        // 사용자 정보 조회
        User user = userService.findUser();
        QuestionRequestDto questionDto = request.questionDto();
        List<OptionRequestDto> optionDtos = request.optionDtos();
        AnswerRequestDto answerDto = request.answerDto();

        // Question 엔티티 생성 및 저장
        Question question = new Question(user, questionDto);
        questionRepository.save(question);

        List<Choice> choices = new ArrayList<>();
        // Option 엔티티 생성 및 저장
        for (OptionRequestDto optionDto : optionDtos) {
            Choice choice = new Choice(question, optionDto);
            choices.add(choice);
            optionRepository.save(choice);
        }

        // Answer 엔티티 생성 및 저장
        Answer answer = new Answer(question, answerDto);
        answerRepository.save(answer);
    }

    @Transactional
    public void deleteQuestion(Long questionId) throws IllegalAccessException {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new EntityNotFoundException("이 id를 가진 문제가 없습니다. " + questionId));
        User user = userService.findUser();
        if (!question.getUser().getId().equals(user.getId())) {
            throw new IllegalAccessException("문제를 지울 권한이 없습니다.");
        }
        answerHistoryRepository.deleteByQuestionId(questionId);
        questionRepository.delete(question);
    }

    public QuestionResponseDto getQuestionById(Long questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new QuestionNotFoundException("문제 게시글을 찾을 수 없습니다. ID: " + questionId));


        List<OptionResponseDto> options = question.getChoices().stream().map(choice -> {
            OptionResponseDto optionResponseDto
                    = new OptionResponseDto(
                            choice.getId(),
                            choice.getNumber(),
                            choice.getContent()
            );
            return optionResponseDto;
        }).collect(Collectors.toList());

        QuestionResponseDto questionResponseDto
                = new QuestionResponseDto(
                question.getUser().getNickname(),
                question.getTitle(),
                question.getContent(),
                question.getQuestionType(),
                question.getImage(),
                question.getUpdatedAt(),
                options,
                question.getIsPastExam()
        );

        return questionResponseDto;
    }

    @Transactional
    public void updateQuestion(Long questionId, QuestionCreateRequest request) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new EntityNotFoundException("해당 id를 가진 문제가 없습니다. " + questionId));

        User user = userService.findUser();
        // userId가 일치하는지 확인
        if (!question.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedException("수정권한이 없습니다.");
        }

        // Question 객체 업데이트
        question.update(request.questionDto());

        question.getChoices().clear(); // 연관된 모든 Choice 엔티티를 컬렉션에서 제거

        // 새 옵션 추가
        for (OptionRequestDto optionDto : request.optionDtos()) {
            Choice choice = new Choice(question, optionDto);
            question.getChoices().add(choice); // 새 Choice 엔티티를 컬렉션에 추가
        }

        // Answer 업데이트
        Answer answer = question.getAnswer();
        if (answer == null) {
            answer = new Answer();
            answer.setQuestion(question);
            question.setAnswer(answer);
        }
        answer.setAnswers(request.answerDto().answers());
        answer.setSubjectiveAnswer(request.answerDto().subjectiveAnswer());
        answer.setImage(request.answerDto().image());
        answer.setCommentary(request.answerDto().commentary());

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
    public boolean solveQuestion(Long questionId, SolveRequestDto solveRequestDto) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found"));
        User user = userService.findUser();

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
        answerHistory.setAnswers(solveRequestDto.answers());
        answerHistory.setSubjectiveAnswer(solveRequestDto.subjectiveAnswer());
        answerHistory.setCorrect(isCorrect);

        answerHistoryRepository.save(answerHistory);

        return isCorrect;
    }

    private boolean checkCorrectness(Answer answer, SolveRequestDto solveRequestDto) {
        if (answer.getAnswers() != null) {
            Set<Integer> correctAnswers = Arrays.stream(answer.getAnswers().split(","))
                    .map(Integer::parseInt)
                    .collect(Collectors.toSet());
            return correctAnswers.equals(solveRequestDto.answers());
        } else {
            return answer.getSubjectiveAnswer().equals(solveRequestDto.subjectiveAnswer());
        }
    }

}

