package com.file_processing.advice;

import com.file_processing.exception.ErrorDTO;
import com.file_processing.exception.AppError;
import com.file_processing.exception.InvalidFileFormatException;
import com.file_processing.exception.InvalidRequestException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.lang.reflect.UndeclaredThrowableException;
import java.util.Collections;
import java.util.Set;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidFileFormatException.class)
    public ResponseEntity<GlobalResponseDTO<?>> handleInvalidFileExceptionHandler(InvalidFileFormatException e) {
        var response = GlobalResponseDTO.builder()
            .response(Collections.emptyList())
            .error(AppError.INVALID_FILE_TYPE.getCode())
            .message(e.getMessage())
            .build();

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(response);
    }

    @ExceptionHandler(UndeclaredThrowableException.class)
    public ResponseEntity<GlobalResponseDTO<?>> handleUndeclaredThrowableException(UndeclaredThrowableException ex) {
        var throwable = ex.getUndeclaredThrowable();
        if (throwable == null) {
            return handleException(ex);
        }

        if (throwable instanceof InvalidRequestException) {
            return handleInvalidRequestException((InvalidRequestException) throwable);
        }

        return handleException((Exception) throwable);
    }

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<GlobalResponseDTO<?>> handleInvalidRequestException(InvalidRequestException e) {
        var response = GlobalResponseDTO.builder()
            .response(Collections.emptyList())
            .error(AppError.INVALID_REQUEST_EXCEPTION.getCode())
            .message(e.getMessage())
            .build();

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<GlobalResponseDTO<?>> handleConstraintViolationException(ConstraintViolationException ex) {
        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
        var errors = violations.stream()
            .map(violation -> ErrorDTO.builder()
                .fieldName(violation.getPropertyPath().toString())
                .errorMessage(violation.getMessage())
                .build())
            .toList();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            GlobalResponseDTO.builder()
                .response(errors)
                .error(AppError.INVALID_REQUEST_EXCEPTION.getMessage())
                .message(AppError.INVALID_REQUEST_EXCEPTION.getMessage())
                .build()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GlobalResponseDTO<?>> handleException(Exception e) {
        var response = GlobalResponseDTO.builder()
            .response(Collections.emptyList())
            .error(AppError.UNKNOWN_EXCEPTION.getCode())
            .message(e.getMessage())
            .build();

        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .contentType(MediaType.APPLICATION_JSON)
            .body(response);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<GlobalResponseDTO<?>> handleMissingParams(MissingServletRequestParameterException ex) {
        String name = ex.getParameterName();
        var error = ErrorDTO.builder()
            .fieldName(name)
            .errorMessage(name + " parameter is missing")
            .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            GlobalResponseDTO.builder()
                .response(error)
                .error(AppError.INVALID_REQUEST_EXCEPTION.getMessage())
                .message(AppError.INVALID_REQUEST_EXCEPTION.getMessage())
                .build()
        );
    }

}
