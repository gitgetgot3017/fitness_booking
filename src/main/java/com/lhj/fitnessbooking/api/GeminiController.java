package com.lhj.fitnessbooking.api;

import com.lhj.fitnessbooking.api.dto.MemberInputRequest;
import com.lhj.fitnessbooking.api.dto.RecommendDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ai")
public class GeminiController {

    private final GeminiService geminiService;

    @GetMapping
    public HttpEntity<RecommendDto> recommendCourse(@ModelAttribute MemberInputRequest request) {

        return new ResponseEntity<>(geminiService.recommendCourse(request), HttpStatus.OK);
    }
}
