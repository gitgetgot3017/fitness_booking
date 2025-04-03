package com.lhj.fitnessbooking.domain.member.exception;

public class NotExistMemberException extends RuntimeException {

    public NotExistMemberException(String message) {
        super(message);
    }
}
