package com.lhj.fitnessbooking.jwt.exception;

public class NotExistAccessTokenException extends RuntimeException {

    public NotExistAccessTokenException(String message) {
        super(message);
    }
}
