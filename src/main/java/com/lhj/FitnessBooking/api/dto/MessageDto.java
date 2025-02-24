package com.lhj.FitnessBooking.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MessageDto {

    private boolean flag; // Gemini를 사용할 것인지 여부
    private String message;
}
