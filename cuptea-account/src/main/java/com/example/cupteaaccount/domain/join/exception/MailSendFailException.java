package com.example.cupteaaccount.domain.join.exception;

public class MailSendFailException extends RuntimeException{

    public MailSendFailException(String message) {
        super(message);
    }

    public MailSendFailException(String message, Throwable cause) {
        super(message, cause);
    }
}
