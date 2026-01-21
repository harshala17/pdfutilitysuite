package com.file_processing.service;

import com.file_processing.exception.AppError;
import com.file_processing.exception.InvalidRequestException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.io.RandomAccessReadBuffer;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdfwriter.compress.CompressParameters;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.JPEGFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openpdf.text.Document;
import org.openpdf.text.PageSize;
import org.openpdf.text.Phrase;
import org.openpdf.text.pdf.PdfPTable;
import org.openpdf.text.pdf.PdfWriter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.LinkedList;
import java.util.List;

@Slf4j
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

    @SneakyThrows
    @Override
    public ResponseEntity<byte[]> imagesToPDF(MultipartFile[] images) {
        try (var doc = new PDDocument()) {
            for (var image : images) {
                // Create a new page for each image
                var page = new PDPage(PDRectangle.A4);
                doc.addPage(page);

                // Create image object from current file's bytes
                var pdImage = PDImageXObject.createFromByteArray(doc, image.getBytes(), image.getOriginalFilename());

                // Scaling logic to fit image on page
                var pageWidth = page.getMediaBox().getWidth();
                var pageHeight = page.getMediaBox().getHeight();
                var imgWidth = pdImage.getWidth();
                var imgHeight = pdImage.getHeight();

                var scale = Math.min((pageWidth - 40) / imgWidth, (pageHeight - 40) / imgHeight);
                var finalWidth = imgWidth * scale;
                var finalHeight = imgHeight * scale;

                var x = (pageWidth - finalWidth) / 2;
                var y = (pageHeight - finalHeight) / 2;

                // Draw current image onto current page
                try (var contentStream = new PDPageContentStream(doc, page)) {
                    contentStream.drawImage(pdImage, x, y, finalWidth, finalHeight);
                }
            }

            var baos = new ByteArrayOutputStream();
            doc.save(baos);
            return getPdfResponse(baos);
        }
    }
}
