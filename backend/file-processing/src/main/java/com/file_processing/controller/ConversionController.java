package com.file_processing.controller;

import com.file_processing.service.ConversionService;
import com.file_processing.validators.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.JsonNode;

@Validated
@RestController
@RequestMapping("/api/v1/convert")
public class ConversionController {

    @Autowired
    private ConversionService conversionService;

    @PostMapping(value = "/images-to-pdf", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<byte[]> imagesToPDF(@RequestParam("images") @ValidImages MultipartFile[] images) {
        return conversionService.imagesToPDF(images);
    }

    @PostMapping(value = "/excel-to-pdf", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<byte[]> excelToPDF(@RequestParam("file") @ValidCSVOrExcel MultipartFile file) {
        return conversionService.excelToPDF(file);
    }

    @PostMapping(value = "/word-to-pdf", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<byte[]> wordToPDF(@RequestParam("file") @ValidDoc MultipartFile file) {
        return conversionService.wordToPDF(file);
    }

    @PostMapping(value = "/ppt-to-pdf", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<byte[]> pptToPDF(@RequestParam("file") @ValidPPT MultipartFile file) {
        return conversionService.pptToPDF(file);
    }

    @PostMapping(value = "/json-to-csv", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<byte[]> jsonToCSV(@RequestBody JsonNode json) {
        return conversionService.jsonToCSV(json);
    }

    @PostMapping(value = "/csv-to-json", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public JsonNode csvToJson(@RequestParam("file") @ValidCSV MultipartFile file) {
        return conversionService.csvToJson(file);
    }
}
