package com.example.demo.controller.exception;

/**
 * Exception to be thrown when datasource does not find data.
 */
public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
