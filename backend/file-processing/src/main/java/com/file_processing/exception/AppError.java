package com.file_processing.exception;

public enum AppError {

    ACCESS_DENIED("FP001", "ACCESS_DENIED"),
    INVALID_FILE_TYPE("FP002", "INVALID_FILE_TYPE"),
    INVALID_REQUEST_EXCEPTION("FP003", "INVALID_REQUEST_EXCEPTION"),
    UNKNOWN_EXCEPTION("FP004", "UNKNOWN_EXCEPTION");

    private final String code;
    private String message;

    AppError(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getCode() {
        return code;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
