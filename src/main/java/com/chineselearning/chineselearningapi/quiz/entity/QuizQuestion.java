package com.chineselearning.chineselearningapi.quiz.entity;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(
        name = "quiz_questions",
        indexes = {
                @Index(
                        name = "idx_question_quiz",
                        columnList = "quiz_id"
                )
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuizQuestion {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;

    @ManyToOne(
            fetch = FetchType.LAZY,
            optional = false
    )
    @JoinColumn(
            name = "quiz_id",
            nullable = false
    )
    private Quiz quiz;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "question_type",
            nullable = false,
            length = 30
    )
    private QuestionType questionType;

    @Column(
            name = "question_text",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String questionText;

    @Column(
            name = "question_pinyin",
            columnDefinition = "TEXT"
    )
    private String questionPinyin;

    @Column(
            name = "question_order",
            nullable = false
    )
    private Integer questionOrder;

    @Column(
            nullable = false
    )
    private Integer points;

    @PrePersist
    public void prePersist() {

        if (questionType == null) {
            questionType =
                    QuestionType.MULTIPLE_CHOICE;
        }

        if (points == null) {
            points = 1;
        }
    }
}