package com.lhj.FitnessBooking.course.exception;

public class EnrollmentLimitExceededException extends RuntimeException {

    public EnrollmentLimitExceededException(String message) {
        super(message);
    }
}
