package com.lhj.FitnessBooking.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Getter
public class GeminiRequest {

    private List<Content> contents;

    public GeminiRequest(String message) {
        Part part = new TextPart(message);
        Content content = new Content(Collections.singletonList(part));
        this.contents = Arrays.asList(content);
    }

    @Getter
    @AllArgsConstructor
    private static class Content {
        private List<Part> parts;
    }

    interface Part {}

    @Getter
    @AllArgsConstructor
    private static class TextPart implements Part {
        public String text;
    }
}
