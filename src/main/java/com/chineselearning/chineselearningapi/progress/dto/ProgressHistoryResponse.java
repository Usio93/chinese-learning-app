package com.chineselearning.chineselearningapi.progress.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ProgressHistoryResponse {

    private Integer days;

    private List<ProgressHistoryItem> history;
}