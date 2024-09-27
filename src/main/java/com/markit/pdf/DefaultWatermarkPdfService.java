package com.markit.pdf;

import com.markit.exceptions.AsyncWatermarkPdfException;
import com.markit.exceptions.ExecutorNotFoundException;
import com.markit.exceptions.WatermarkPdfServiceNotFoundException;
import com.markit.api.WatermarkMethod;
import com.markit.api.WatermarkPosition;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * @author Oleg Cheban
 * @since 1.0
 */
public class DefaultWatermarkPdfService implements WatermarkPdfService {
    private static final Log logger = LogFactory.getLog(DefaultWatermarkPdfService.class);
    private final Optional<PdfWatermarker> drawService;
    private final Optional<OverlayPdfWatermarker> overlayService;
    private final Optional<Executor> executorService;

    public DefaultWatermarkPdfService(PdfWatermarker pdfWatermarker, OverlayPdfWatermarker overlayService, Executor es) {
        this.drawService = Optional.ofNullable(pdfWatermarker);
        this.overlayService = Optional.ofNullable(overlayService);
        this.executorService = Optional.ofNullable(es);
    }

    public DefaultWatermarkPdfService(PdfWatermarker pdfWatermarker, OverlayPdfWatermarker overlayService) {
        this.drawService = Optional.ofNullable(pdfWatermarker);
        this.overlayService = Optional.ofNullable(overlayService);
        this.executorService = Optional.empty();
    }

    @Override
    public byte[] watermark(byte[] sourceImageBytes, boolean isAsyncMode, String text, int textSize, Color color, float dpi, boolean trademark, WatermarkMethod method, WatermarkPosition position) throws IOException {
        if (!drawService.isPresent() || !overlayService.isPresent()){
            logger.error("Incorrect configuration. An empty service");
            throw new WatermarkPdfServiceNotFoundException();
        }

        switch (method){
            case OVERLAY:
                return overlay(sourceImageBytes, text, textSize, color, trademark, position);
            case DRAW:
                return draw(sourceImageBytes, isAsyncMode, text, textSize, color, dpi, trademark, position);
            default:
                throw new RuntimeException("watermark method is undefined");
        }
    }

    private byte[] overlay(byte[] sourceImageBytes, String text, int textSize, Color color, boolean trademark, WatermarkPosition position) throws IOException {
        try(PDDocument document = PDDocument.load(sourceImageBytes)) {
            int numberOfPages = document.getNumberOfPages();
            for (int pageIndex = 0; pageIndex < numberOfPages; pageIndex++) {
                overlayService.get().watermark(document, pageIndex, text, textSize, color, position, trademark);
            }
            removeSecurity(document);
            return convertPDDocumentToByteArray(document);
        }
    }

    private byte[] draw(byte[] sourceImageBytes, boolean isAsyncMode, String text, int textSize, Color color, float dpi, boolean trademark, WatermarkPosition position) throws IOException {
        try(PDDocument document = PDDocument.load(sourceImageBytes)) {
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            int numberOfPages = document.getNumberOfPages();
            if (isAsyncMode) {
                async(document, pdfRenderer, numberOfPages, text, textSize, color, dpi, trademark, position);
            } else {
                sync(document, pdfRenderer, numberOfPages, text, textSize, color, dpi, trademark, position);
            }
            removeSecurity(document);
            return convertPDDocumentToByteArray(document);
        }
    }

    private void async(PDDocument document, PDFRenderer pdfRenderer, int numberOfPages, String watermarkText, int textSize, Color watermarkColor, float dpi, boolean trademark, WatermarkPosition position){
        if (!executorService.isPresent()){
            logger.error("An empty executor");
            throw new ExecutorNotFoundException();
        }

        List<CompletableFuture<Void>> futures = new ArrayList<>();
        for (int pageIndex = 0; pageIndex < numberOfPages; pageIndex++) {
            int finalPageIndex = pageIndex;
            futures.add(
                CompletableFuture.runAsync(
                        () -> {
                            try {
                                drawService.get().watermark(document, pdfRenderer, finalPageIndex, dpi, watermarkText, textSize, watermarkColor, trademark, position);
                            } catch (IOException e) {
                                logger.error(
                                        String.format(
                                                "An error occurred during watermarking on page number %d",
                                                finalPageIndex), e);
                                throw new AsyncWatermarkPdfException(e);
                            }
                        },
                        executorService.get()
                )
            );
        }
        var allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture<?>[0]));
        allOf.join();
    }

    private void sync(PDDocument document, PDFRenderer pdfRenderer, int numberOfPages, String text, int textSize, Color color, float dpi, boolean trademark, WatermarkPosition position) throws IOException {
        for (int pageIndex = 0; pageIndex < numberOfPages; pageIndex++) {
            drawService.get().watermark(document, pdfRenderer, pageIndex, dpi, text, textSize, color, trademark, position);
        }
    }

    private byte[] convertPDDocumentToByteArray(PDDocument document) throws IOException {
        try (var baos = new ByteArrayOutputStream()) {
            document.save(baos);
            return baos.toByteArray();
        }
    }

    private void removeSecurity(PDDocument document) {
        if (document.isEncrypted()){
            document.setAllSecurityToBeRemoved(true);
        }
    }
}
