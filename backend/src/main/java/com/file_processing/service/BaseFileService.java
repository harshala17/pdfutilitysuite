package com.file_processing.service;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayOutputStream;

public class BaseFileService {
    protected ResponseEntity<byte[]> getPdfResponse(ByteArrayOutputStream outputStream) {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "merged.pdf");
        headers.setContentLength(outputStream.toByteArray().length);

        return ResponseEntity.ok()
            .headers(headers)
            .body(outputStream.toByteArray());
    }
}
