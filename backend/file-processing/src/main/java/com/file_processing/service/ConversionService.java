package com.file_processing.service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.JsonNode;

public interface ConversionService {
    ResponseEntity<byte[]> imagesToPDF(MultipartFile[] images);

    ResponseEntity<byte[]> excelToPDF(MultipartFile file);

    ResponseEntity<byte[]> wordToPDF(MultipartFile file);

    ResponseEntity<byte[]> pptToPDF(MultipartFile file);

    ResponseEntity<byte[]> jsonToCSV(JsonNode json);

    JsonNode csvToJson(MultipartFile file);
}
