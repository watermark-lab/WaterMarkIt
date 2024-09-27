package com.markit;

import com.markit.api.*;
import com.markit.api.FileType;
import com.markit.api.WatermarkMethod;
import com.markit.api.WatermarkPosition;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WatermarkTest {
    private PDDocument plainDocument;
    private PDDocument landscapeDocument;

    @BeforeEach
    void initDocument() {
        plainDocument = new PDDocument();
        plainDocument.addPage(new PDPage());

        landscapeDocument = new PDDocument();
        PDPage landscapePage = new PDPage(PDRectangle.A4);
        landscapePage.setMediaBox(new PDRectangle(PDRectangle.A4.getHeight(), PDRectangle.A4.getWidth()));
        landscapeDocument.addPage(landscapePage);
    }

    @Test
    void givenPlainPdf_whenDrawMethod_thenMakeWatermarkedPdf() throws IOException {
        // When
        byte[] result = WatermarkService.create(
                            Executors.newFixedThreadPool(
                                Runtime.getRuntime().availableProcessors()
                            )
                )
                .file(plainDocument, FileType.PDF)
                .text("Sample Watermark")
                .textSize(50)
                .position(WatermarkPosition.TOP_LEFT)
                .method(WatermarkMethod.DRAW)
                .trademark()
                .color(Color.BLUE)
                .dpi(150f)
                .apply();

        // Then
        assertNotNull(result, "The resulting byte array should not be null");
        assertTrue(result.length > 0, "The resulting byte array should not be empty");
        outputFile(result, "DrawPlainPdf.pdf");
        plainDocument.close();
    }

    @Test
    void givenPlainPdf_whenOverlayMethod_thenMakeWatermarkedPdf() throws IOException {
        // When
        byte[] result =
                WatermarkService.create()
                        .file(plainDocument, FileType.PDF)
                        .text("Sample Watermark")
                        .textSize(20)
                        .method(WatermarkMethod.OVERLAY) //Overlay mode isn't resource-consuming, so a thread pool isn't necessary.
                        .position(WatermarkPosition.TOP_RIGHT)
                        .trademark()
                        .color(Color.BLUE)
                        .apply();

        // Then
        assertNotNull(result, "The resulting byte array should not be null");
        assertTrue(result.length > 0, "The resulting byte array should not be empty");
        outputFile(result, "OverlayPlainPdf.pdf");
        plainDocument.close();
    }

    @Test
    void givenLandscapePdf_whenDrawMethod_thenMakeWatermarkedPdf() throws IOException {
        // When
        byte[] result = WatermarkService.create(
                        Executors.newFixedThreadPool(
                                Runtime.getRuntime().availableProcessors()
                        )
                )
                .file(landscapeDocument, FileType.PDF)
                .text("Sample Watermark")
                .position(WatermarkPosition.CENTER)
                .method(WatermarkMethod.DRAW)
                .color(Color.BLUE)
                .dpi(150f)
                .apply();

        // Then
        assertNotNull(result, "The resulting byte array should not be null");
        assertTrue(result.length > 0, "The resulting byte array should not be empty");
        outputFile(result, "DrawLandscapePdf.pdf");
        plainDocument.close();
    }

    @Test
    void givenLandscapePdf_whenOverlayMethod_thenMakeWatermarkedPdf() throws IOException {
        // When
        byte[] result =
                WatermarkService.create()
                        .file(landscapeDocument, FileType.PDF)
                        .text("Sample Watermark")
                        .method(WatermarkMethod.OVERLAY) //Overlay mode isn't resource-consuming, so a thread pool isn't necessary.
                        .position(WatermarkPosition.BOTTOM_LEFT)
                        .trademark()
                        .color(Color.BLUE)
                        .apply();

        // Then
        assertNotNull(result, "The resulting byte array should not be null");
        assertTrue(result.length > 0, "The resulting byte array should not be empty");
        outputFile(result, "OverlayLandscapePdf.pdf");
        plainDocument.close();
    }

    private void outputFile(byte[] result, String filename) throws IOException {
        File outputFile = new File(filename);
        Files.write(outputFile.toPath(), result);
    }
}
