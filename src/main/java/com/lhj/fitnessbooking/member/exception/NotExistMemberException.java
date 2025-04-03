package com.lhj.FitnessBooking.member.exception;

public class NotExistMemberException extends RuntimeException {

    public NotExistMemberException(String message) {
        super(message);
    }
}
