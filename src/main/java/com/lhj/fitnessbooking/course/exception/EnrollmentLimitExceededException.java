package com.lhj.fitnessbooking.course.exception;

public class EnrollmentLimitExceededException extends RuntimeException {

    public EnrollmentLimitExceededException(String message) {
        super(message);
    }
}
