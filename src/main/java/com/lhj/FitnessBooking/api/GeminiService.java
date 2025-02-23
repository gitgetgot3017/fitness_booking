package com.lhj.FitnessBooking.api;

import com.lhj.FitnessBooking.api.dto.GeminiRequest;
import com.lhj.FitnessBooking.api.dto.GeminiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class GeminiService {

    private final GeminiInterface geminiInterface;

    public String recommendCourse(String message) {

        GeminiRequest geminiRequest = new GeminiRequest(message);
        GeminiResponse response = geminiInterface.getCompletion("gemini-pro", geminiRequest);

        return response.getCandidates()
                .stream()
                .findFirst().flatMap(candidate -> candidate.getContent().getParts()
                        .stream()
                        .findFirst()
                        .map(GeminiResponse.TextPart::getText))
                .orElse(null);
    }
}
