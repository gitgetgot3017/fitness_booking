package com.lhj.fitnessbooking.domain.course.exception;

public class CannotAccessException extends RuntimeException {

    public CannotAccessException(String message) {
        super(message);
    }
}
