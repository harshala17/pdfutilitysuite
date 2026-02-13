package com.file_processing.controller;

import com.file_processing.service.WordService;
import com.file_processing.validators.annotations.ValidDoc;
import com.file_processing.validators.annotations.ValidDocs;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/docx")
public class WordController {

    @Autowired
    private WordService wordService;

    @PostMapping(value = "/merge", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<byte[]> mergeWordFiles(@RequestParam("files") @ValidDocs MultipartFile[] files) {
        return wordService.merge(files);
    }

    @PostMapping(value = "/merge-refined", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<byte[]> mergeRefined(@RequestParam("file") @ValidDoc MultipartFile file, @RequestParam("removeIndexes") @NotEmpty List<Integer> removeIndexes) {
        return wordService.mergeRefined(file, removeIndexes);
    }
}
