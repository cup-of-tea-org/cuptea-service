package com.example.cupteaapi.exceptionhandler.exception;

import lombok.NoArgsConstructor;

public class FriendAlreadyExistException extends RuntimeException{

    public FriendAlreadyExistException(String message) {
        super(message);
    }

    public FriendAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
