package com.chineselearning.chineselearningapi.quiz.service;

import com.chineselearning.chineselearningapi.quiz.dto.*;

import com.chineselearning.chineselearningapi.quiz.entity.*;

import com.chineselearning.chineselearningapi.quiz.repository.*;

import com.chineselearning.chineselearningapi.user.entity.User;
import com.chineselearning.chineselearningapi.user.repository.UserRepository;
import com.chineselearning.chineselearningapi.progress.service.SkillAnalyticsService;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class QuizService {
    private final SkillAnalyticsService
            skillAnalyticsService;
    private final UserRepository userRepository;

    private final QuizRepository quizRepository;

    private final QuizQuestionRepository
            quizQuestionRepository;

    private final QuizOptionRepository
            quizOptionRepository;

    private final QuizAttemptRepository
            quizAttemptRepository;

    // =========================================================
    // GET QUIZZES BY LESSON
    // =========================================================

    @Transactional(readOnly = true)
    public List<QuizResponse> getByLesson(
            Long lessonId
    ) {

        return quizRepository
                .findAllByLessonIdAndActiveTrue(
                        lessonId
                )
                .stream()
                .map(this::toQuizResponse)
                .toList();
    }

    // =========================================================
    // GET QUIZ DETAIL
    // =========================================================

    @Transactional(readOnly = true)
    public QuizResponse getQuiz(
            Long quizId
    ) {

        Quiz quiz =
                getActiveQuiz(
                        quizId
                );

        return toQuizResponse(
                quiz
        );
    }

    // =========================================================
    // SUBMIT QUIZ
    // =========================================================

    @Transactional
    public QuizResultResponse submitQuiz(
            String email,
            Long quizId,
            QuizSubmitRequest request
    ) {

        User user =
                userRepository
                        .findByEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User not found"
                                )
                        );

        Quiz quiz =
                getActiveQuiz(
                        quizId
                );

        List<QuizQuestion> questions =
                quizQuestionRepository
                        .findAllByQuizIdOrderByQuestionOrderAsc(
                                quizId
                        );

        if (questions.isEmpty()) {
            throw new RuntimeException(
                    "Quiz has no questions"
            );
        }

        /*
         * Chuyển answers thành:
         *
         * questionId -> optionId
         */
        Map<Long, Long> submittedAnswers =
                new HashMap<>();

        for (QuizAnswerRequest answer :
                request.getAnswers()) {

            if (submittedAnswers.containsKey(
                    answer.getQuestionId()
            )) {

                throw new RuntimeException(
                        "Duplicate answer for question: "
                                + answer.getQuestionId()
                );
            }

            submittedAnswers.put(
                    answer.getQuestionId(),
                    answer.getOptionId()
            );
        }

        int correctAnswers = 0;
        int answeredQuestions = 0;

        // =====================================================
        // CHECK EACH QUESTION
        // =====================================================

        for (QuizQuestion question :
                questions) {

            Long selectedOptionId =
                    submittedAnswers.get(
                            question.getId()
                    );

            if (selectedOptionId == null) {
                continue;
            }

            answeredQuestions++;

            QuizOption selectedOption =
                    quizOptionRepository
                            .findById(
                                    selectedOptionId
                            )
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Quiz option not found: "
                                                    + selectedOptionId
                                    )
                            );

            /*
             * Security check:
             * option phải thuộc đúng question.
             */
            if (!selectedOption
                    .getQuestion()
                    .getId()
                    .equals(
                            question.getId()
                    )) {

                throw new RuntimeException(
                        "Option does not belong to question"
                );
            }

            if (Boolean.TRUE.equals(
                    selectedOption.getCorrect()
            )) {

                correctAnswers++;
            }
        }

        // =====================================================
        // VALIDATE QUESTION IDS
        // =====================================================

        Set<Long> validQuestionIds =
                new HashSet<>();

        for (QuizQuestion question :
                questions) {

            validQuestionIds.add(
                    question.getId()
            );
        }

        for (Long submittedQuestionId :
                submittedAnswers.keySet()) {

            if (!validQuestionIds.contains(
                    submittedQuestionId
            )) {

                throw new RuntimeException(
                        "Question does not belong to this quiz: "
                                + submittedQuestionId
                );
            }
        }

        // =====================================================
        // SCORE
        // =====================================================

        int totalQuestions =
                questions.size();

        double score =
                correctAnswers * 100.0
                        / totalQuestions;

        score =
                Math.round(
                        score * 100.0
                ) / 100.0;

        boolean passed =
                score >= quiz.getPassScore();

        LocalDateTime submittedAt =
                LocalDateTime.now();

        // =====================================================
        // SAVE ATTEMPT
        // =====================================================

        QuizAttempt attempt =
                QuizAttempt.builder()

                        .user(user)

                        .quiz(quiz)

                        .totalQuestions(
                                totalQuestions
                        )

                        .correctAnswers(
                                correctAnswers
                        )

                        .score(score)

                        .passed(passed)

                        .submittedAt(
                                submittedAt
                        )

                        .build();

        quizAttemptRepository.save(
                attempt
        );
        skillAnalyticsService.recordQuizResult(
                user,
                quiz.getLesson().getHskLevel(),
                quiz.getLesson().getSkill(),
                totalQuestions,
                correctAnswers
        );

        return QuizResultResponse
                .builder()

                .attemptId(
                        attempt.getId()
                )

                .quizId(
                        quiz.getId()
                )

                .quizTitle(
                        quiz.getTitle()
                )

                .totalQuestions(
                        totalQuestions
                )

                .answeredQuestions(
                        answeredQuestions
                )

                .correctAnswers(
                        correctAnswers
                )

                .wrongAnswers(
                        totalQuestions
                                - correctAnswers
                )

                .score(score)

                .passScore(
                        quiz.getPassScore()
                )

                .passed(passed)

                .submittedAt(
                        submittedAt
                )

                .build();
    }

    // =========================================================
    // ATTEMPT HISTORY
    // =========================================================

    @Transactional(readOnly = true)
    public List<QuizAttemptResponse> getMyAttempts(
            String email
    ) {

        User user =
                userRepository
                        .findByEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User not found"
                                )
                        );

        return quizAttemptRepository
                .findAllByUserIdOrderBySubmittedAtDesc(
                        user.getId()
                )
                .stream()
                .map(this::toAttemptResponse)
                .toList();
    }

    // =========================================================
    // MAPPER
    // =========================================================

    private QuizResponse toQuizResponse(
            Quiz quiz
    ) {

        List<QuizQuestionResponse>
                questions =
                quizQuestionRepository
                        .findAllByQuizIdOrderByQuestionOrderAsc(
                                quiz.getId()
                        )
                        .stream()
                        .map(this::toQuestionResponse)
                        .toList();

        return QuizResponse.builder()

                .id(
                        quiz.getId()
                )

                .lessonId(
                        quiz.getLesson().getId()
                )

                .title(
                        quiz.getTitle()
                )

                .description(
                        quiz.getDescription()
                )

                .passScore(
                        quiz.getPassScore()
                )

                .totalQuestions(
                        questions.size()
                )

                .questions(
                        questions
                )

                .build();
    }

    private QuizQuestionResponse
    toQuestionResponse(
            QuizQuestion question
    ) {

        List<QuizOptionResponse> options =
                quizOptionRepository
                        .findAllByQuestionIdOrderByOptionOrderAsc(
                                question.getId()
                        )
                        .stream()

                        /*
                         * Không map selectedOption.correct.
                         */
                        .map(option ->
                                QuizOptionResponse
                                        .builder()

                                        .id(
                                                option.getId()
                                        )

                                        .optionText(
                                                option.getOptionText()
                                        )

                                        .optionOrder(
                                                option.getOptionOrder()
                                        )

                                        .build()
                        )

                        .toList();

        return QuizQuestionResponse
                .builder()

                .id(
                        question.getId()
                )

                .questionType(
                        question.getQuestionType()
                )

                .questionText(
                        question.getQuestionText()
                )

                .questionPinyin(
                        question.getQuestionPinyin()
                )

                .questionOrder(
                        question.getQuestionOrder()
                )

                .points(
                        question.getPoints()
                )

                .options(options)

                .build();
    }

    private QuizAttemptResponse
    toAttemptResponse(
            QuizAttempt attempt
    ) {

        Quiz quiz =
                attempt.getQuiz();

        return QuizAttemptResponse
                .builder()

                .attemptId(
                        attempt.getId()
                )

                .quizId(
                        quiz.getId()
                )

                .quizTitle(
                        quiz.getTitle()
                )

                .lessonId(
                        quiz.getLesson().getId()
                )

                .hskLevel(
                        quiz.getLesson()
                                .getHskLevel()
                )

                .score(
                        attempt.getScore()
                )

                .passed(
                        attempt.getPassed()
                )

                .correctAnswers(
                        attempt.getCorrectAnswers()
                )

                .totalQuestions(
                        attempt.getTotalQuestions()
                )

                .submittedAt(
                        attempt.getSubmittedAt()
                )

                .build();
    }

    private Quiz getActiveQuiz(
            Long quizId
    ) {

        return quizRepository
                .findByIdAndActiveTrue(
                        quizId
                )
                .orElseThrow(() ->
                        new RuntimeException(
                                "Quiz not found"
                        )
                );
    }
}