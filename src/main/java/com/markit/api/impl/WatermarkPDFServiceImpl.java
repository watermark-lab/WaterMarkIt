package com.markit.api.impl;

import com.markit.api.AbstractWatermarkService;
import com.markit.api.WatermarkPDFService;
import com.markit.api.WatermarkingMethod;
import com.markit.exceptions.ClosePDFDocumentException;
import com.markit.pdf.WatermarkPdfServiceBuilder;
import org.apache.pdfbox.pdmodel.PDDocument;
import com.markit.api.WatermarkPDFService.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.function.Predicate;

/**
 * @author Oleg Cheban
 * @since 1.3.0
 */
public class WatermarkPDFServiceImpl
        extends AbstractWatermarkService<WatermarkPDFService, WatermarkPDFBuilder, TextBasedWatermarkBuilder, WatermarkPositionStepPDFBuilder>
        implements WatermarkPDFService, WatermarkPDFBuilder, TextBasedWatermarkBuilder, WatermarkPositionStepPDFBuilder {
    private final PDDocument document;
    public WatermarkPDFServiceImpl(PDDocument pdfDoc, Executor executor) {
        this.document = pdfDoc;
        watermarkHandler =
                (watermarks) ->
                        WatermarkPdfServiceBuilder.build(executor)
                                .watermark(pdfDoc, watermarks);
    }

    @Override
    public WatermarkPDFBuilder method(WatermarkingMethod watermarkingMethod) {
        currentWatermark.setMethod(watermarkingMethod);
        return this;
    }

    @Override
    public WatermarkPDFBuilder dpi(int dpi) {
        currentWatermark.setDpi(Optional.of((float) dpi));
        return this;
    }

    @Override
    public WatermarkPDFBuilder documentFilter(Predicate<PDDocument> predicate) {
        currentWatermark.setDocumentPredicate(predicate);
        return this;
    }

    @Override
    public WatermarkPDFBuilder pageFilter(Predicate<Integer> predicate) {
        currentWatermark.setPagePredicate(predicate);
        return this;
    }

    @NotNull
    @Override
    public byte[] apply() {
        var res = super.apply();
        closeDocument();
        return res;
    }

    private void closeDocument() {
        try {
            this.document.close();
        } catch (IOException e) {
            throw new ClosePDFDocumentException("Failed to close the document", e);
        }
    }
}
