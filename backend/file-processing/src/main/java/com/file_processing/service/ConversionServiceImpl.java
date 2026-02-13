package com.file_processing.service;

import dev.inaka.libreoffice.LibreOfficeOptions;
import dev.inaka.libreoffice.LibreOfficePageProperties;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;

@Slf4j
@Service
public class ConversionServiceImpl extends BaseFileService implements ConversionService {

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

    @Override
    public ResponseEntity<byte[]> excelToPDF(MultipartFile file) {
        var pageProps = new LibreOfficePageProperties.Builder()
            .addLandscape(true)            // Prevents column cutoff
            .addSinglePageSheets(false)     // Fits all columns to one page width
            .addSkipEmptyPages(true)
            .build();

        var options = new LibreOfficeOptions.Builder()
            .addLosslessImageCompression(true)
            .build();

        var outputStream = convertFile(file, pageProps, options);
        return getPdfResponse(outputStream);
    }

    @Override
    public ResponseEntity<byte[]> wordToPDF(MultipartFile file) {
        var pageProps = new LibreOfficePageProperties.Builder()
            .addSkipEmptyPages(true)
            .addExportNotesInMargin(false)
            .build();

        var options = new LibreOfficeOptions.Builder()
            .addLosslessImageCompression(true)
            .build();

        var outputStream = convertFile(file, pageProps, options);
        return getPdfResponse(outputStream);
    }
}
