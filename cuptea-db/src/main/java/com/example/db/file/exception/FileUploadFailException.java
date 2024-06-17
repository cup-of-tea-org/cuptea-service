package com.example.db.file.exception;

public class FileUploadFailException extends RuntimeException {
    public FileUploadFailException() {
    }

    public FileUploadFailException(String message) {
        super(message);
    }

    public FileUploadFailException(String message, Throwable cause) {
        super(message, cause);
    }
}
