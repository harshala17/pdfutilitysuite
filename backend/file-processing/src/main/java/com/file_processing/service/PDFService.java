package com.file_processing.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PDFService {
    ResponseEntity<byte[]> merge(MultipartFile[] files);

    ResponseEntity<byte[]> mergeRefined(MultipartFile file, List<Integer> removeIndexes);
}
