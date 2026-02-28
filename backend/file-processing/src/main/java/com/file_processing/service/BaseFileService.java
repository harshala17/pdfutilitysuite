package com.file_processing.service;

import com.file_processing.exception.AppError;
import com.file_processing.exception.InvalidRequestException;
import dev.inaka.Jotenberg;
import dev.inaka.libreoffice.LibreOfficeOptions;
import dev.inaka.libreoffice.LibreOfficePageProperties;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Collections;

public class BaseFileService {

    @Value("${api.gotenberg.uri:http://localhost:3000/}")
    private String gotenbergURL;

    protected ResponseEntity<byte[]> getPdfResponse(ByteArrayOutputStream outputStream) {
        byte[] data = outputStream.toByteArray();
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "merged.pdf");
        headers.setContentLength(data.length);

        return ResponseEntity.ok()
            .headers(headers)
            .body(data);
    }

    protected ResponseEntity<byte[]> getCsvResponse(byte[] data) {
        var headers = new HttpHeaders();

        // Set Media Type for CSV
        headers.setContentType(MediaType.parseMediaType("text/csv"));

        // Set Content-Disposition to trigger browser download
        headers.setContentDispositionFormData("attachment", "data.csv");

        // Important for large files to hint the size to the client
        headers.setContentLength(data.length);

        return ResponseEntity.ok()
            .headers(headers)
            .body(data);
    }

    protected ResponseEntity<byte[]> getPdfResponse(ByteArrayOutputStream out, String filename) {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.attachment()
            .filename(filename)
            .build());

        return ResponseEntity.ok()
            .headers(headers)
            .body(out.toByteArray());
    }

    protected ResponseEntity<byte[]> getWordResponse(ByteArrayOutputStream outputStream) {
        byte[] data = outputStream.toByteArray();
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.wordprocessingml.document"));
        headers.setContentDispositionFormData("attachment", "merged.docx");
        headers.setContentLength(data.length);

        return ResponseEntity.ok()
            .headers(headers)
            .body(data);
    }

    @SneakyThrows
    protected ByteArrayOutputStream convertFile(MultipartFile file, LibreOfficePageProperties properties, LibreOfficeOptions options) {
        // 1. Prepare Jotenberg client and temp file
        var out = new ByteArrayOutputStream();
        var client = new Jotenberg(gotenbergURL);
        File tempFile = File.createTempFile("gotenberg_", "_" + file.getOriginalFilename());
        file.transferTo(tempFile);

        try {
            // 2. Execute conversion with provided properties and options
            try (var response = client.convertWithLibreOffice(
                Collections.singletonList(tempFile), properties, options)) {

                if (response.getStatusLine().getStatusCode() != 200) {
                    throw new RuntimeException("Gotenberg Error: " + response.getStatusLine().getStatusCode());
                }

                // 3. Return the raw byte array for further processing (merge or response)
                response.getEntity().getContent().transferTo(out);
                return out;
            }
        } finally {
            tempFile.delete(); // 4. Mandatory cleanup
        }
    }
}
