package com.markit;

import com.markit.services.*;
import com.markit.services.impl.FileType;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.Executors;

import static com.markit.services.impl.WatermarkMethod.DRAW;
import static com.markit.services.impl.WatermarkMethod.OVERLAY;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WatermarkTest {
    private PDDocument document;

    @BeforeEach
    void initDocument() {
        document = new PDDocument();
        document.addPage(new PDPage());
    }

    @Test
    void testDrawPdfMethod() throws IOException {
        byte[] result = WatermarkService.create(
                            Executors.newFixedThreadPool(
                                Runtime.getRuntime().availableProcessors()
                            )
                )
                .file(document, FileType.PDF)
                .watermarkText("Sample Watermark")
                .watermarkMethod(DRAW)
                .trademark()
                .color(Color.BLUE)
                .dpi(150f)
                .apply();


        assertNotNull(result, "The resulting byte array should not be null");
        assertTrue(result.length > 0, "The resulting byte array should not be empty");
        document.close();
    }

    @Test
    void testOverlayPdfMethod() throws IOException {
        byte[] result =
                WatermarkService
                //Overlay mode isn't resource-consuming, so a thread pool isn't necessary.
                .create()
                .file(document, FileType.PDF)
                .watermarkText("Sample Watermark")
                .watermarkMethod(OVERLAY)
                .color(Color.GREEN)
                .apply();

        assertNotNull(result, "The resulting byte array should not be null");
        assertTrue(result.length > 0, "The resulting byte array should not be empty");
        document.close();
    }

    private void outputFile(byte[] result, String filename) throws IOException {
        File outputFile = new File(filename);
        Files.write(outputFile.toPath(), result);
    }
}
