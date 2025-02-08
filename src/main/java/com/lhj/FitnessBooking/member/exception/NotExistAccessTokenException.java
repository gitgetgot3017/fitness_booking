package com.lhj.FitnessBooking.member.exception;

public class NotExistAccessTokenException extends RuntimeException {

    public NotExistAccessTokenException(String message) {
        super(message);
    }
}
