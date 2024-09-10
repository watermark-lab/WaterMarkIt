package com.markit;

import com.markit.services.WatermarkService;
import com.markit.services.impl.FileType;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.IOException;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WatermarkTest {
    private PDDocument document;
    private WatermarkService watermarkService;

    @BeforeEach
    void setUp() {
        document = new PDDocument();
        document.addPage(new PDPage());
        watermarkService =
                WatermarkService.create(
                        Executors.newFixedThreadPool(
                                Runtime.getRuntime().availableProcessors()
                        )
                );
    }

    @Test
    void testApplyMethod() throws IOException {
        byte[] result = watermarkService
                .file(document)
                .fileType(FileType.PDF)
                .watermarkText("NASA")
                .trademark()
                .color(new Color(255, 0, 0))
                .dpi(150f)
                .apply();

        assertNotNull(result, "The resulting byte array should not be null");
        assertTrue(result.length > 0, "The resulting byte array should not be empty");

        document.close();
        //File outputFile = new File("res.pdf");
        //Files.write(outputFile.toPath(), result);
    }
}
