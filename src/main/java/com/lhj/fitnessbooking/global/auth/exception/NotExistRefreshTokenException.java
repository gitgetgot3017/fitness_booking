package com.lhj.fitnessbooking.global.auth.exception;

public class NotExistRefreshTokenException extends RuntimeException {

    public NotExistRefreshTokenException(String message) {
        super(message);
    }
}
