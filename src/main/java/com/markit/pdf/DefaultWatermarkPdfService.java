package com.markit.pdf;

import com.markit.core.WatermarkAttributes;
import com.markit.core.WatermarkingMethod;
import com.markit.exceptions.ExecutorNotFoundException;
import com.markit.pdf.draw.DrawPdfWatermarker;
import com.markit.pdf.overlay.OverlayPdfWatermarker;
import com.markit.servicelocator.ServiceFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

/**
 * @author Oleg Cheban
 * @since 1.0
 */
public class DefaultWatermarkPdfService implements WatermarkPdfService {
    private static final Log logger = LogFactory.getLog(DefaultWatermarkPdfService.class);
    private final Optional<Executor> executorService;

    public DefaultWatermarkPdfService(Executor es) {
        this.executorService = Optional.ofNullable(es);
    }

    @Override
    public byte[] watermark(PDDocument document, List<WatermarkAttributes> attrs) throws IOException {
        applyWatermark(document, attrs, WatermarkingMethod.DRAW, this::draw);
        applyWatermark(document, attrs, WatermarkingMethod.OVERLAY, this::overlay);
        removeSecurity(document);
        return convertPDDocumentToByteArray(document);
    }

    private void applyWatermark(PDDocument document, List<WatermarkAttributes> attrs,
                                WatermarkingMethod method, PdfWatermarkHandler action) throws IOException {
        var filteredAttrs = attrs.stream()
                .filter(WatermarkAttributes::getVisible)
                .filter(attr -> attr.getDocumentPredicate().test(document))
                .filter(attr -> attr.getMethod().equals(method))
                .collect(Collectors.toList());
        if (!filteredAttrs.isEmpty()) {
            action.apply(document, filteredAttrs);
        }
    }

    private void overlay(PDDocument document, List<WatermarkAttributes> attrs) throws IOException {
        var overlayService = (OverlayPdfWatermarker) ServiceFactory.getInstance().getService(OverlayPdfWatermarker.class);
        int numberOfPages = document.getNumberOfPages();
        for (int pageIndex = 0; pageIndex < numberOfPages; pageIndex++) {
            List<WatermarkAttributes> filteredAttrs = filterAttrsByPageIndex(attrs, pageIndex);
            if (!filteredAttrs.isEmpty()){
                overlayService.watermark(document, pageIndex, filteredAttrs);
            }
        }
    }

    private void draw(PDDocument document, List<WatermarkAttributes> attrs) {
        int numberOfPages = document.getNumberOfPages();
        if (executorService.isEmpty()) {
            sync(document, numberOfPages, attrs);
        } else {
            async(document, numberOfPages, attrs);
        }
    }

    private void async(PDDocument document, int numberOfPages, List<WatermarkAttributes> attrs){
        if (executorService.isEmpty()){
            logger.error("An empty executor");
            throw new ExecutorNotFoundException();
        }

        List<CompletableFuture<Void>> futures = new ArrayList<>();
        for (int pageIndex = 0; pageIndex < numberOfPages; pageIndex++) {
            final int pIndex = pageIndex;
            futures.add(
                CompletableFuture.runAsync(
                        () -> {
                            try {
                                draw(document, pIndex, attrs);
                            } catch (IOException e) {
                                logPageException(e, pIndex);
                            }
                        },
                        executorService.get()
                )
            );
        }
        var allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture<?>[0]));
        allOf.join();
    }

    private void sync(PDDocument document, int numberOfPages, List<WatermarkAttributes> attrs) {
        for (int pageIndex = 0; pageIndex < numberOfPages; pageIndex++) {
            try {
                draw(document, pageIndex, attrs);
            } catch (IOException e) {
                logPageException(e, pageIndex);
            }
        }
    }

    private void draw(PDDocument document, int pageIndex, List<WatermarkAttributes> attrs) throws IOException {
        var drawService = (DrawPdfWatermarker) ServiceFactory.getInstance().getService(DrawPdfWatermarker.class);
        List<WatermarkAttributes> filteredAttrs = filterAttrsByPageIndex(attrs, pageIndex);
        if (!filteredAttrs.isEmpty()) drawService.watermark(document, pageIndex, filteredAttrs);
    }

    private static List<WatermarkAttributes> filterAttrsByPageIndex(List<WatermarkAttributes> attrs, int pIndex) {
        return attrs.stream()
                .filter(attr -> attr.getPagePredicate().test(pIndex))
                .collect(Collectors.toList());
    }

    private void logPageException(Exception e, int pageIndex){
        logger.error(String.format("An error occurred during watermarking on page number %d", pageIndex), e);
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
