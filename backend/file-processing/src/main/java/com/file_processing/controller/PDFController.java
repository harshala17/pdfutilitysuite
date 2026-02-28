package com.file_processing.controller;

import com.file_processing.service.PDFService;
import com.file_processing.validators.annotations.ValidPDF;
import com.file_processing.validators.annotations.ValidPDFs;
import jakarta.validation.constraints.NotEmpty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/v1/pdf")
public class PDFController {

    @Autowired
    private PDFService pdfService;

    @PostMapping(value = "/merge", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<byte[]> mergePDFs(@RequestParam("files") @ValidPDFs MultipartFile[] files) {
        return pdfService.merge(files);
    }

    @PostMapping(value = "/merge-refined", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<byte[]> mergeRefined(@RequestParam("file") @ValidPDF MultipartFile file, @RequestParam("removeIndexes") @NotEmpty List<Integer> removeIndexes) {
        return pdfService.mergeRefined(file, removeIndexes);
    }
}
