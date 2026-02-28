package com.file_processing.service;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.file_processing.exception.AppError;
import com.file_processing.exception.InvalidRequestException;
import com.github.wnameless.json.flattener.JsonFlattener;
import com.github.wnameless.json.unflattener.JsonUnflattener;
import dev.inaka.libreoffice.LibreOfficeOptions;
import dev.inaka.libreoffice.LibreOfficePageProperties;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
@Service
public class ConversionServiceImpl extends BaseFileService implements ConversionService {

    private final ObjectMapper mapper = new ObjectMapper();
    private final CsvMapper csvMapper = new CsvMapper();

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
        return convertToPDF(file);
    }

    @Override
    public ResponseEntity<byte[]> pptToPDF(MultipartFile file) {
        return convertToPDF(file);
    }

    @SneakyThrows
    @Override
    public ResponseEntity<byte[]> jsonToCSV(JsonNode json) {
        if (json.isEmpty()) {
            throw new InvalidRequestException(AppError.INVALID_REQUEST_EXCEPTION, "Json cannot be empty");
        }

        var rootNode = mapper.readTree(String.valueOf(json));

        var flatList = new ArrayList<Map<String, Object>>();
        var headers = new LinkedHashSet<String>();

        // Flatten data and collect headers
        if (rootNode.isArray()) {
            for (JsonNode node : rootNode) {
                addToFlatList(node.toString(), flatList, headers);
            }
        } else if (rootNode.isObject()) {
            addToFlatList(rootNode.toString(), flatList, headers);
        }

        // Create Schema
        var schemaBuilder = CsvSchema.builder();
        headers.forEach(header -> schemaBuilder.addColumn(String.valueOf(header)));
        var schema = schemaBuilder.build().withHeader();

        // Convert to byte array
        var csvMapper = new CsvMapper();
        var csvBytes = csvMapper.writer(schema).writeValueAsBytes(flatList);

        return getCsvResponse(csvBytes);
    }

    @SneakyThrows
    @Override
    public JsonNode csvToJson(MultipartFile file) {
        var schema = CsvSchema.emptySchema().withHeader();

        List<Map<String, String>> flatData;
        try (var is = file.getInputStream();
             MappingIterator<Map<String, String>> it = csvMapper
                 .readerFor(Map.class)
                 .with(schema)
                 .readValues(is)) {

            flatData = it.readAll(); // Streams values into the list
        }

        if (flatData.isEmpty()) {
            throw new InvalidRequestException(AppError.INVALID_REQUEST_EXCEPTION, "File is empty");
        }

        // 1. Convert the flat list to a JSON string
        var flatJson = mapper.writeValueAsString(flatData);

        // 2. Unflatten to restore nested structures (e.g., "a.b": 1 -> {"a": {"b": 1}})
        var nestedJson = JsonUnflattener.unflatten(flatJson);

        return mapper.readTree(nestedJson);
    }

    private ResponseEntity<byte[]> convertToPDF(MultipartFile file) {
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

    private void addToFlatList(String json, List<Map<String, Object>> list, Collection<String> headers) {
        var flatMap = JsonFlattener.flattenAsMap(json);
        list.add(flatMap);
        headers.addAll(flatMap.keySet());
    }
}
