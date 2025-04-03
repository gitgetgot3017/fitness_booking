package com.lhj.fitnessbooking.domain.gemini.handler;

import com.lhj.fitnessbooking.domain.gemini.controller.GeminiController;
import com.lhj.fitnessbooking.domain.gemini.exception.NotExistModelException;
import com.lhj.fitnessbooking.global.exception.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = GeminiController.class)
public class GeminiExController {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ErrorResponse showErrorMessage(NotExistModelException e) {
        return new ErrorResponse("gemini", e.getMessage());
    }
}
