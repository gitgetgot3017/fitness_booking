package com.lhj.fitnessbooking.course.exception;

public class ClassCapacityExceededException extends RuntimeException {

    public ClassCapacityExceededException(String message) {
        super(message);
    }
}
