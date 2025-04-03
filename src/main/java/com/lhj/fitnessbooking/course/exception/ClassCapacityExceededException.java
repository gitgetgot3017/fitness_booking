package com.lhj.FitnessBooking.course.exception;

public class ClassCapacityExceededException extends RuntimeException {

    public ClassCapacityExceededException(String message) {
        super(message);
    }
}
