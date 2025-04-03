package com.lhj.fitnessbooking.domain.course.exception;

public class NotExistCourseException extends RuntimeException {

    public NotExistCourseException(String message) {
        super(message);
    }
}
