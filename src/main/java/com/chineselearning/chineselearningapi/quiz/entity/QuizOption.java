package com.chineselearning.chineselearningapi.quiz.entity;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(
        name = "quiz_options",
        indexes = {
                @Index(
                        name = "idx_option_question",
                        columnList = "question_id"
                )
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuizOption {

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
            name = "question_id",
            nullable = false
    )
    private QuizQuestion question;

    @Column(
            name = "option_text",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String optionText;

    @Column(
            name = "option_order",
            nullable = false
    )
    private Integer optionOrder;

    /*
     * Không được trả field này về API lấy đề.
     */
    @Column(
            name = "is_correct",
            nullable = false
    )
    private Boolean correct;
}