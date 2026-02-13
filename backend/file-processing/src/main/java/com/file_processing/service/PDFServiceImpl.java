package com.file_processing.service;

import com.file_processing.exception.AppError;
import com.file_processing.exception.InvalidRequestException;
import lombok.SneakyThrows;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.io.RandomAccessReadBuffer;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.util.LinkedList;
import java.util.List;

@Service
public class PDFServiceImpl extends BaseFileService implements PDFService {

    @SneakyThrows
    @Override
    public ResponseEntity<byte[]> merge(MultipartFile[] files) {
        if (files.length == 1) {
            throw new InvalidRequestException(AppError.INVALID_REQUEST_EXCEPTION, "Upload more than 1 file");
        }
        var pdfMergerUtility = new PDFMergerUtility();
        var outputStream = new ByteArrayOutputStream();

        try (outputStream) {
            pdfMergerUtility.setDestinationStream(outputStream);
            var bytes = new LinkedList<byte[]>();

            for (var file : files) {
                var fileBytes = file.getBytes();
                if (ObjectUtils.isEmpty(fileBytes)) {
                    continue;
                }
                bytes.add(file.getBytes());
            }

            if (bytes.isEmpty()) {
                throw new InvalidRequestException(AppError.INVALID_REQUEST_EXCEPTION, "Invalid File");
            }

            for (var fileByte : bytes) {
                pdfMergerUtility.addSource(new RandomAccessReadBuffer(fileByte));
            }

            pdfMergerUtility.mergeDocuments(null);
        }
        return getPdfResponse(outputStream);
    }

    @SneakyThrows
    @Override
    public ResponseEntity<byte[]> mergeRefined(MultipartFile file, List<Integer> removeIndexes) {
        try (var source = Loader.loadPDF(file.getBytes());
             var target = new PDDocument()) {

            // Only add the pages the user kept
            for (var removeIndex : removeIndexes) {
                target.removePage(source.getPage(removeIndex));
            }

            var baos = new ByteArrayOutputStream();
            target.save(baos);

            return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .body(baos.toByteArray());
        }
    }
}
