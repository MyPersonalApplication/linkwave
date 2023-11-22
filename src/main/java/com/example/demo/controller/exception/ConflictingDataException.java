package com.example.demo.controller.exception;

public class ConflictingDataException extends RuntimeException {
    public ConflictingDataException(String message) {
        super(message);
    }

    public ConflictingDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
