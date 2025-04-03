package com.lhj.fitnessbooking.jwt.exception;

public class NotExistRefreshTokenException extends RuntimeException {

    public NotExistRefreshTokenException(String message) {
        super(message);
    }
}
