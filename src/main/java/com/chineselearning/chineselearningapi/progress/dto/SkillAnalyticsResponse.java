package com.chineselearning.chineselearningapi.progress.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class SkillAnalyticsResponse {

    private List<SkillProgressResponse> skills;
}