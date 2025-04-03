package com.lhj.fitnessbooking.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {

    private String domain;
    private String message;
}
