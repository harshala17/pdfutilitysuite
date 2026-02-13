package com.file_processing.advice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GlobalResponseDTO<T> {
    private T response;
    private String error;
    private String message;
}
