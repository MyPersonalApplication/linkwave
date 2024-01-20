package com.example.demo.controller.exception;

public class DataUnavailableException extends RuntimeException {
    public DataUnavailableException(String message) {
        super(message);
    }

    public DataUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
