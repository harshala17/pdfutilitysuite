package com.file_processing.advice;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice
public class GlobalResponseHandler implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return returnType.getContainingClass().getPackageName().startsWith("com.file_processing");
    }

    @Override
    public Object beforeBodyWrite(
        Object body, MethodParameter returnType, MediaType selectedContentType,
        Class<? extends HttpMessageConverter<?>> selectedConverterType,
        ServerHttpRequest request, ServerHttpResponse response
    ) {
        if (body instanceof byte[]) {
            return body;
        }

        if (body instanceof GlobalResponseDTO) {
            return body;
        }

        var apiResponse = ResponseEntity.status(HttpStatus.OK).body(
            GlobalResponseDTO.builder()
                .response(body)
                .message("success")
                .build()
        );

        return apiResponse.getBody();
    }
}
