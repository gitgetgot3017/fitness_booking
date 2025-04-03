package com.lhj.fitnessbooking.global.auth.exception;

public class NotExistAccessTokenException extends RuntimeException {

    public NotExistAccessTokenException(String message) {
        super(message);
    }
}
