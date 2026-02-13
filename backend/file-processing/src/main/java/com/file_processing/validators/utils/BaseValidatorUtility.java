package com.file_processing.validators.utils;

import org.springframework.web.multipart.MultipartFile;

public class BaseValidatorUtility {

    private BaseValidatorUtility() {
        throw new IllegalStateException("Cannot instantiate BaseValidatorUtility class");
    }

    public static boolean isValidDoc(MultipartFile file) {
        var docxMime = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";

        var contentType = file.getContentType();
        var fileName = file.getOriginalFilename();

        // 1. Strict MIME Type Check
        boolean isValidMime = docxMime.equalsIgnoreCase(contentType);

        // 2. Strict Extension Check (Remove .doc)
        boolean isValidExtension = fileName != null && fileName.toLowerCase().endsWith(".docx");

        // Return true only if it matches the modern Word format
        return isValidMime || isValidExtension;
    }

    public static boolean isValidCsvOrExcel(MultipartFile file) {
        var contentType = file.getContentType();
        var filename = file.getOriginalFilename() != null ? file.getOriginalFilename().toLowerCase() : "";

        // Check Content-Types
        var isCsv = "text/csv".equalsIgnoreCase(contentType) || "application/vnd.ms-excel".equalsIgnoreCase(contentType);
        var isXlsx = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet".equalsIgnoreCase(contentType);

        // Check Extensions (as a fallback)
        var hasCsvExt = filename.endsWith(".csv");
        var hasExcelExt = filename.endsWith(".xlsx") || filename.endsWith(".xls");

        return isCsv || isXlsx || hasCsvExt || hasExcelExt;
    }

    public static boolean isValidPDF(MultipartFile file) {
        return "application/pdf".equalsIgnoreCase(file.getContentType());
    }
}
