package com.chineselearning.chineselearningapi.lesson.service;

import com.chineselearning.chineselearningapi.lesson.dto.LessonContentResponse;
import com.chineselearning.chineselearningapi.lesson.dto.LessonDetailResponse;

import com.chineselearning.chineselearningapi.lesson.entity.Lesson;
import com.chineselearning.chineselearningapi.lesson.entity.LessonContent;

import com.chineselearning.chineselearningapi.lesson.repository.LessonContentRepository;
import com.chineselearning.chineselearningapi.lesson.repository.LessonRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LessonContentService {

    private final LessonRepository lessonRepository;

    private final LessonContentRepository
            lessonContentRepository;

    @Transactional(readOnly = true)
    public LessonDetailResponse getLessonContent(
            Long lessonId
    ) {

        Lesson lesson =
                lessonRepository
                        .findById(lessonId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Lesson not found"
                                )
                        );

        List<LessonContentResponse> contents =
                lessonContentRepository
                        .findAllByLessonIdOrderByContentOrderAsc(
                                lessonId
                        )
                        .stream()
                        .map(this::toContentResponse)
                        .toList();

        return LessonDetailResponse
                .builder()

                .id(
                        lesson.getId()
                )

                .title(
                        lesson.getTitle()
                )

                .description(
                        lesson.getDescription()
                )

                .hskLevel(
                        lesson.getHskLevel()
                )

                .skill(
                        lesson.getSkill()
                )

                .estimatedMinutes(
                        lesson.getEstimatedMinutes()
                )

                .premiumOnly(
                        lesson.getPremiumOnly()
                )

                .contents(contents)

                .build();
    }

    private LessonContentResponse
    toContentResponse(
            LessonContent content
    ) {

        return LessonContentResponse
                .builder()

                .id(
                        content.getId()
                )

                .contentType(
                        content.getContentType()
                )

                .title(
                        content.getTitle()
                )

                .textContent(
                        content.getTextContent()
                )

                .pinyinContent(
                        content.getPinyinContent()
                )

                .translationVi(
                        content.getTranslationVi()
                )

                .audioUrl(
                        content.getAudioUrl()
                )

                .videoUrl(
                        content.getVideoUrl()
                )

                .contentOrder(
                        content.getContentOrder()
                )

                .build();
    }
}