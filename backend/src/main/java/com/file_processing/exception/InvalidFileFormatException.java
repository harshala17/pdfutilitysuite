package com.file_processing.exception;

public class InvalidFileFormatException extends BaseException {
    public InvalidFileFormatException(AppError error, String message) {
        super(error, message);
    }
}
