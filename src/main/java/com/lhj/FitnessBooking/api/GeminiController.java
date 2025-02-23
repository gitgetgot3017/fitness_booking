package com.lhj.FitnessBooking.api;

import com.lhj.FitnessBooking.api.dto.MemberInputRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ai")
public class GeminiController {

    private final GeminiService geminiService;

    @GetMapping
    public String recommendCourse(@ModelAttribute MemberInputRequest request) {

        String message = getMessage(request);
        return geminiService.recommendCourse(message);
    }

    private String getMessage(MemberInputRequest request) {

        return "오늘 컨디션은 " + request.getCondition() +", 목표는 " + request.getGoal() + "일 때 적절한 요가를 추천해줘";
    }
}
