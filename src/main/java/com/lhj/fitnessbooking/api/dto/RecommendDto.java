package com.lhj.fitnessbooking.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RecommendDto {

    private String geminiSaid; // Gemini의 응답
    private String courseName; // Gemini의 응답에서 추출한 수업명
}
