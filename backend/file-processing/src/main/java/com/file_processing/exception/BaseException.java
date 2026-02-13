package com.file_processing.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class BaseException extends Exception {

    private final AppError error;
    private final String message;

    public BaseException(AppError error, String message) {
        super(error.getMessage());
        this.error = error;
        this.message = message;
    }
}