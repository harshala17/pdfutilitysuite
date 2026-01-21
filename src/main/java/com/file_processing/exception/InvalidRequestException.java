package com.file_processing.exception;

public class InvalidRequestException extends BaseException {

    public InvalidRequestException(AppError error, String message) {
        super(error, message);
    }
}
