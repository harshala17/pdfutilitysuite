package com.file_processing.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface ConversionService {
    ResponseEntity<byte[]> imagesToPDF(MultipartFile[] images);

    ResponseEntity<byte[]> excelToPDF(MultipartFile file);

    ResponseEntity<byte[]> wordToPDF(MultipartFile file);
}
