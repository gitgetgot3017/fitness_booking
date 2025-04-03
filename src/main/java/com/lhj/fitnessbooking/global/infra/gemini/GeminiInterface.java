package com.lhj.fitnessbooking.global.infra.gemini;

import com.lhj.fitnessbooking.domain.gemini.dto.GeminiRequest;
import com.lhj.fitnessbooking.domain.gemini.dto.GeminiResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange("/v1beta/models")
public interface GeminiInterface {

    @PostExchange("/{model}:generateContent")
    GeminiResponse getCompletion(@PathVariable("model") String model, @RequestBody GeminiRequest request);
}
