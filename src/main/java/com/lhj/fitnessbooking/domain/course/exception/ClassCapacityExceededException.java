package com.lhj.fitnessbooking.domain.course.exception;

public class ClassCapacityExceededException extends RuntimeException {

    public ClassCapacityExceededException(String message) {
        super(message);
    }
}
