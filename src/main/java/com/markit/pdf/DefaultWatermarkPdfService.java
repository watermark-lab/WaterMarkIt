package com.markit.pdf;

import com.markit.api.WatermarkAttributes;
import com.markit.exceptions.AsyncWatermarkPdfException;
import com.markit.exceptions.ExecutorNotFoundException;
import com.markit.exceptions.UnsupportedWatermarkMethodException;
import com.markit.exceptions.WatermarkPdfServiceNotFoundException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import java.io.ByteArrayOutputStream;
import java.io.File;
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
    public byte[] watermark(byte[] sourceImageBytes, boolean isAsyncMode, WatermarkAttributes attr) throws IOException {
        try(PDDocument document = PDDocument.load(sourceImageBytes)) {
            return watermark(document, isAsyncMode, attr);
        }
    }

    @Override
    public byte[] watermark(File file, boolean isAsyncMode, WatermarkAttributes attr) throws IOException {
        try(PDDocument document = PDDocument.load(file)) {
            return watermark(document, isAsyncMode, attr);
        }
    }

    @Override
    public byte[] watermark(PDDocument document, boolean isAsyncMode, WatermarkAttributes attr) throws IOException {
        if (drawService.isEmpty() || overlayService.isEmpty()){
            logger.error("Incorrect configuration. An empty service");
            throw new WatermarkPdfServiceNotFoundException();
        }

        switch (attr.getMethod()){
            case OVERLAY:
                return overlay(document, attr);
            case DRAW:
                return draw(document, isAsyncMode, attr);
            default: throw new UnsupportedWatermarkMethodException("Unsupported watermark method: " + attr.getMethod());
        }
    }

    private byte[] overlay(PDDocument document, WatermarkAttributes attr) throws IOException {
        int numberOfPages = document.getNumberOfPages();
        for (int pageIndex = 0; pageIndex < numberOfPages; pageIndex++) {
            overlayService.get().watermark(document, pageIndex, attr);
        }
        removeSecurity(document);
        return convertPDDocumentToByteArray(document);
    }

    private byte[] draw(PDDocument document, boolean isAsyncMode, WatermarkAttributes attr) throws IOException {
        PDFRenderer pdfRenderer = new PDFRenderer(document);
        int numberOfPages = document.getNumberOfPages();
        if (isAsyncMode) {
            async(document, pdfRenderer, numberOfPages, attr);
        } else {
            sync(document, pdfRenderer, numberOfPages, attr);
        }
        removeSecurity(document);
        return convertPDDocumentToByteArray(document);
    }

    private void async(PDDocument document, PDFRenderer pdfRenderer, int numberOfPages, WatermarkAttributes attr){
        if (executorService.isEmpty()){
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
                                drawService.get().watermark(document, pdfRenderer, finalPageIndex, attr);
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

    private void sync(PDDocument document, PDFRenderer pdfRenderer, int numberOfPages, WatermarkAttributes attr) throws IOException {
        for (int pageIndex = 0; pageIndex < numberOfPages; pageIndex++) {
            drawService.get().watermark(document, pdfRenderer, pageIndex, attr);
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
