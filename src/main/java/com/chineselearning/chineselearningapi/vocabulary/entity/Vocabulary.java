package com.chineselearning.chineselearningapi.vocabulary.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "vocabularies",
        indexes = {
                @Index(
                        name = "idx_vocabulary_hsk",
                        columnList = "hsk_level"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vocabulary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name = "hanzi",
            nullable = false,
            length = 100
    )
    private String hanzi;

    @Column(
            name = "pinyin",
            nullable = false,
            length = 150
    )
    private String pinyin;

    @Column(
            name = "meaning_vi",
            length = 500
    )
    private String meaningVi;

    @Column(
            name = "meaning_en",
            length = 500
    )
    private String meaningEn;

    @Column(
            name = "meaning_zh",
            length = 500
    )
    private String meaningZh;

    @Column(
            name = "example_sentence",
            length = 1000
    )
    private String exampleSentence;

    @Column(
            name = "example_pinyin",
            length = 1000
    )
    private String examplePinyin;

    @Column(
            name = "example_meaning_vi",
            length = 1000
    )
    private String exampleMeaningVi;

    @Column(
            name = "audio_url",
            length = 1000
    )
    private String audioUrl;

    @Column(
            name = "hsk_level",
            nullable = false
    )
    private Integer hskLevel;

    @Builder.Default
    @Column(
            name = "is_active",
            nullable = false
    )
    private Boolean active = true;
}