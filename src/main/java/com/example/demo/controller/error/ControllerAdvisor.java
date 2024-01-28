package com.example.demo.controller.error;

import com.fasterxml.jackson.core.JsonParseException;
import com.example.demo.controller.exception.*;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class ControllerAdvisor {
    @ResponseBody
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ApiError notFoundHandler(NotFoundException ex) {
        return ApiError.builder()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .status(HttpStatus.NOT_FOUND)
                .message(ex.getLocalizedMessage())
                .build();
    }

    @ResponseBody
    @ExceptionHandler(ConflictingDataException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    ApiError conflictRequestHandler(ConflictingDataException ex) {
        return ApiError.builder()
                .statusCode(HttpStatus.CONFLICT.value())
                .status(HttpStatus.CONFLICT)
                .message(ex.getLocalizedMessage())
                .build();
    }

    @ResponseBody
    @ExceptionHandler({InvalidDataException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ApiError badPathHandler(Exception ex) {
        return ApiError.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .status(HttpStatus.BAD_REQUEST)
                .message(ex.getLocalizedMessage())
                .build();
    }


    @ResponseBody
    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    ApiError catchAll(Exception ex) {
        log.error("unexpected problem", ex);
        return ApiError.builder()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .message(ex.getLocalizedMessage())
                .build();
    }

    @ResponseBody
    @ExceptionHandler({DataUnavailableException.class})
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    ApiError handleDataUnavailableError(Exception ex) {
        return ApiError.builder()
                .statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .message(ex.getLocalizedMessage())
                .build();
    }

    @ResponseBody
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ApiError handleBindException(BindException ex) {
        String message = ex.getFieldErrors().stream()
                .map(fieldError -> String.format("%s(%s)", fieldError.getField(), fieldError.getDefaultMessage()))
                .collect(Collectors.joining(", "));
        log.error("invalid request: {}", message, ex);
        return ApiError.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .status(HttpStatus.BAD_REQUEST)
                .message(ex.getLocalizedMessage())
                .build();
    }

    @ResponseBody
    @ExceptionHandler({
            IllegalArgumentException.class,
            MethodArgumentTypeMismatchException.class,
            MethodArgumentNotValidException.class,
            JsonParseException.class,
            MissingServletRequestParameterException.class,
            HttpMessageNotReadableException.class,
            ConstraintViolationException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ApiError badRequestHandler(Exception ex) {
        return ApiError.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .status(HttpStatus.BAD_REQUEST)
                .message(ex.getLocalizedMessage())
                .build();
    }

    @ResponseBody
    @ExceptionHandler({
            AuthenticationException.class,
            ExpiredJwtException.class,
    })
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    ApiError authenticationHandler(Exception ex) {
        return ApiError.builder()
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .status(HttpStatus.UNAUTHORIZED)
                .message(ex.getLocalizedMessage())
                .build();
    }

    @ResponseBody
    @ExceptionHandler({
            AccessDeniedException.class
    })
    @ResponseStatus(HttpStatus.FORBIDDEN)
    ApiError accessDeniedHandler(Exception ex) {
        return ApiError.builder()
                .statusCode(HttpStatus.FORBIDDEN.value())
                .status(HttpStatus.FORBIDDEN)
                .message(ex.getLocalizedMessage())
                .build();
    }
}
