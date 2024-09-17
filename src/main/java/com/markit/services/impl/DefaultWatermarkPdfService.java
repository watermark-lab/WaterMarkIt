package com.markit.services.impl;

import com.markit.exceptions.AsyncWatermarkPdfException;
import com.markit.exceptions.ExecutorNotFoundException;
import com.markit.exceptions.WatermarkPdfServiceNotFoundException;
import com.markit.services.PdfWatermarkDrawService;
import com.markit.services.PdfWatermarkOverlayService;
import com.markit.services.WatermarkPdfService;
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
    private final Optional<PdfWatermarkDrawService> drawService;
    private final Optional<PdfWatermarkOverlayService> overlayService;
    private final Optional<Executor> executorService;

    public DefaultWatermarkPdfService(PdfWatermarkDrawService pdfWatermarkDrawService, PdfWatermarkOverlayService overlayService, Executor es) {
        this.drawService = Optional.ofNullable(pdfWatermarkDrawService);
        this.overlayService = Optional.ofNullable(overlayService);
        this.executorService = Optional.ofNullable(es);
    }

    public DefaultWatermarkPdfService(PdfWatermarkDrawService pdfWatermarkDrawService, PdfWatermarkOverlayService overlayService) {
        this.drawService = Optional.ofNullable(pdfWatermarkDrawService);
        this.overlayService = Optional.ofNullable(overlayService);
        this.executorService = Optional.empty();
    }

    @Override
    public byte[] watermark(byte[] sourceImageBytes, Boolean isAsyncMode, String watermarkText, Color watermarkColor, float dpi, Boolean trademark, WatermarkMethod method) throws IOException {
        if (!drawService.isPresent() || !overlayService.isPresent()){
            logger.error("Incorrect configuration. An empty service");
            throw new WatermarkPdfServiceNotFoundException();
        }

        switch (method){
            case OVERLAY:
                return overlay(sourceImageBytes, watermarkText, watermarkColor, trademark);
            case DRAW:
                return draw(sourceImageBytes, isAsyncMode, watermarkText, watermarkColor, dpi, trademark);
            default:
                throw new RuntimeException("watermark method is undefined");
        }
    }

    private byte[] overlay(byte[] sourceImageBytes, String watermarkText, Color watermarkColor, Boolean trademark) throws IOException {
        try(PDDocument document = PDDocument.load(sourceImageBytes)) {
            int numberOfPages = document.getNumberOfPages();
            for (int pageIndex = 0; pageIndex < numberOfPages; pageIndex++) {
                overlayService.get().watermark(document, pageIndex, watermarkText, watermarkColor, trademark);
            }
            removeSecurity(document);
            return convertPDDocumentToByteArray(document);
        }
    }

    private byte[] draw(byte[] sourceImageBytes, Boolean isAsyncMode, String watermarkText, Color watermarkColor, float dpi, Boolean trademark) throws IOException {
        try(PDDocument document = PDDocument.load(sourceImageBytes)) {
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            int numberOfPages = document.getNumberOfPages();
            if (isAsyncMode) {
                async(document, pdfRenderer, numberOfPages, watermarkText, watermarkColor, dpi, trademark);
            } else {
                sync(document, pdfRenderer, numberOfPages, watermarkText, watermarkColor, dpi, trademark);
            }
            removeSecurity(document);
            return convertPDDocumentToByteArray(document);
        }
    }

    private void async(PDDocument document, PDFRenderer pdfRenderer, int numberOfPages, String watermarkText, Color watermarkColor, float dpi, Boolean trademark){
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
                                drawService.get().watermark(document, pdfRenderer, finalPageIndex, dpi, watermarkText, watermarkColor, trademark);
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

    private void sync(PDDocument document, PDFRenderer pdfRenderer, int numberOfPages, String watermarkText, Color watermarkColor, float dpi, Boolean trademark) throws IOException {
        for (int pageIndex = 0; pageIndex < numberOfPages; pageIndex++) {
            drawService.get().watermark(document, pdfRenderer, pageIndex, dpi, watermarkText, watermarkColor, trademark);
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
