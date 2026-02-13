package com.file_processing.service;

import jakarta.xml.bind.JAXBElement;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.docx4j.jaxb.Context;
import org.docx4j.openpackaging.contenttype.ContentType;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.PartName;
import org.docx4j.openpackaging.parts.WordprocessingML.AltChunkType;
import org.docx4j.openpackaging.parts.WordprocessingML.AlternativeFormatInputPart;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.Br;
import org.docx4j.wml.P;
import org.docx4j.wml.R;
import org.docx4j.wml.STBrType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class WordServiceImpl extends BaseFileService implements WordService {

    @Autowired
    private ConversionService conversionService;

    @Autowired
    private PDFService pdfService;

    @SneakyThrows
    @Override
    public ResponseEntity<byte[]> merge(MultipartFile[] files) {
        // 1. Initialize master with the FIRST file in the sequence
        WordprocessingMLPackage master;
        try (var is = files[0].getInputStream()) {
            master = WordprocessingMLPackage.load(is);
        }
        var mainPart = master.getMainDocumentPart();

        // 2. Loop through the rest of the files in sequence
        for (var i = 1; i < files.length; i++) {
            var currentFile = files[i];

            // Add a Section Break to keep orientation/margins "As Is"
            addSectionBreak(mainPart);

            // Create the part for the next document
            var afip = new AlternativeFormatInputPart(new PartName("/part" + i + ".docx"));

            try (var partStream = currentFile.getInputStream()) {
                afip.setBinaryData(partStream.readAllBytes());
            }

            afip.setContentType(new ContentType(AltChunkType.WordprocessingML.getContentType()));

            // FIX: Capture the Relationship object to avoid the deprecated ID call
            var rel = mainPart.addTargetPart(afip);

            // Create the AltChunk and link it to the part
            var ac = Context.getWmlObjectFactory().createCTAltChunk();
            ac.setId(rel.getId());

            mainPart.addObject(ac);
        }

        // 3. Save to byte array and return response
        var baos = new ByteArrayOutputStream();
        master.save(baos);

        return getWordResponse(baos);
    }

    @SneakyThrows
    @Override
    public ResponseEntity<byte[]> mergeRefined(MultipartFile file, List<Integer> removeIndexes) {
        WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(file.getInputStream());
        List<Object> content = wordMLPackage.getMainDocumentPart().getContent();

        // 1. Segment content by page boundaries
        List<List<Object>> pages = new ArrayList<>();
        List<Object> currentPage = new ArrayList<>();

        for (Object obj : content) {
            currentPage.add(obj);
            if (containsPageBreak(obj)) {
                pages.add(new ArrayList<>(currentPage));
                currentPage.clear();
            }
        }
        if (!currentPage.isEmpty()) pages.add(currentPage);

        // 2. Remove specified pages (descending order)
        removeIndexes.sort(Collections.reverseOrder());
        for (int index : removeIndexes) {
            if (index >= 0 && index < pages.size()) {
                pages.remove(index);
            }
        }

        // 3. Rebuild document
        content.clear();
        for (List<Object> page : pages) {
            content.addAll(page);
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        wordMLPackage.save(outputStream);
        return getWordResponse(outputStream);
    }

    private void addSectionBreak(MainDocumentPart mainPart) {
        var factory = Context.getWmlObjectFactory();
        var p = factory.createP();
        var pPr = factory.createPPr();
        var sectPr = factory.createSectPr();
        var type = factory.createSectPrType();

        type.setVal("nextPage"); // Forces "As Is" layout on new page
        sectPr.setType(type);

        pPr.setSectPr(sectPr);
        p.setPPr(pPr);
        mainPart.addObject(p);
    }

    private boolean containsPageBreak(Object obj) {
        // Unwrap JAXB elements if necessary
        Object localObj = (obj instanceof JAXBElement) ? ((JAXBElement<?>) obj).getValue() : obj;

        if (localObj instanceof P) {
            P paragraph = (P) localObj;
            for (Object pContent : paragraph.getContent()) {
                if (pContent instanceof R) {
                    R run = (R) pContent;
                    for (Object rContent : run.getContent()) {
                        // Check for explicit page break <w:br w:type="page"/>
                        if (rContent instanceof Br && ((Br) rContent).getType() == STBrType.PAGE) {
                            return true;
                        }
                    }
                }
            }
        }
        // Note: Section breaks can also start new pages, but are often stored in SectPr
        // at the end of the last paragraph of a section.
        return false;
    }
}
