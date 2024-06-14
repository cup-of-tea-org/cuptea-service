package com.example.cupteaaccount.domain.join.exception;

public class UserJoinFailException extends RuntimeException {
    public UserJoinFailException(String message) {
        super(message);
    }

    public UserJoinFailException(String message, Throwable cause) {
        super(message, cause);
    }
}
