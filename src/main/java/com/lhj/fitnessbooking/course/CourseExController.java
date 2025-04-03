package com.lhj.fitnessbooking.course;

import com.lhj.fitnessbooking.course.exception.*;
import com.lhj.fitnessbooking.reservation.exception.ReservationFailException;
import com.lhj.fitnessbooking.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = CourseController.class)
public class CourseExController {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({NotExistCourseException.class, CannotAccessException.class, NotExistCourseException.class, EnrollmentLimitExceededException.class, DuplicateEnrollmentException.class, CourseExpirationException.class})
    public ErrorResponse showErrorMessage(RuntimeException e) {
        return new ErrorResponse("course", e.getMessage());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler
    public ErrorResponse showErrorMessage(ReservationFailException e) {
        return new ErrorResponse("course", e.getMessage());
    }
}
