package com.lhj.FitnessBooking.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {

    private String domain;
    private String message;
}
