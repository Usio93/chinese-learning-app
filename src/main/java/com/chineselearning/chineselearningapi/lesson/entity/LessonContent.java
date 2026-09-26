package com.chineselearning.chineselearningapi.lesson.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "lesson_contents",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_lesson_content_order",
                        columnNames = {
                                "lesson_id",
                                "content_order"
                        }
                )
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LessonContent {

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
            name = "lesson_id",
            nullable = false
    )
    private Lesson lesson;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "content_type",
            nullable = false,
            length = 30
    )
    private LessonContentType contentType;

    @Column(
            nullable = false,
            length = 200
    )
    private String title;

    @Column(
            name = "text_content",
            columnDefinition = "TEXT"
    )
    private String textContent;

    @Column(
            name = "pinyin_content",
            columnDefinition = "TEXT"
    )
    private String pinyinContent;

    @Column(
            name = "translation_vi",
            columnDefinition = "TEXT"
    )
    private String translationVi;

    @Column(
            name = "audio_url"
    )
    private String audioUrl;

    @Column(
            name = "video_url"
    )
    private String videoUrl;

    @Column(
            name = "content_order",
            nullable = false
    )
    private Integer contentOrder;
}