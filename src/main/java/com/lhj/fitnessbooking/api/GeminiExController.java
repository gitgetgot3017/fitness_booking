package com.lhj.FitnessBooking.api;

import com.lhj.FitnessBooking.api.exception.NotExistModelException;
import com.lhj.FitnessBooking.response.ErrorResponse;
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
