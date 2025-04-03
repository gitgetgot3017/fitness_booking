package com.lhj.fitnessbooking.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {

    private String domain;
    private String message;
}
