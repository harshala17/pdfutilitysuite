package com.file_processing.controller;

import com.file_processing.service.ConversionService;
import com.file_processing.validators.annotations.ValidCSVOrExcel;
import com.file_processing.validators.annotations.ValidDoc;
import com.file_processing.validators.annotations.ValidImages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
}
