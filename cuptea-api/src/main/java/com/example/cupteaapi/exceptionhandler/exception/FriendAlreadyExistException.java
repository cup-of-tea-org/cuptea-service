package com.example.cupteaapi.exceptionhandler.exception;

public class FriendAlreadyExistException extends RuntimeException{

    public FriendAlreadyExistException(String message) {
        super(message);
    }

    public FriendAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
