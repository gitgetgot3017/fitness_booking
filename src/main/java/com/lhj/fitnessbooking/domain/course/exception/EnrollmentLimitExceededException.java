package com.lhj.fitnessbooking.domain.course.exception;

public class EnrollmentLimitExceededException extends RuntimeException {

    public EnrollmentLimitExceededException(String message) {
        super(message);
    }
}
