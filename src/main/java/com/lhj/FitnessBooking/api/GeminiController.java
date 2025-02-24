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
        return geminiService.recommendCourse(request);
    }
}
