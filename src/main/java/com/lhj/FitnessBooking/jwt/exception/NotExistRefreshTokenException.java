package com.lhj.FitnessBooking.jwt.exception;

public class NotExistRefreshTokenException extends RuntimeException {

    public NotExistRefreshTokenException(String message) {
        super(message);
    }
}
