package com.markit.api.impl;

import com.markit.api.AbstractWatermarkService;
import com.markit.api.WatermarkAttributes;
import com.markit.api.WatermarkPDFService;
import com.markit.api.WatermarkingMethod;
import com.markit.pdf.WatermarkPdfServiceBuilder;
import org.apache.pdfbox.pdmodel.PDDocument;
import com.markit.api.WatermarkPDFService.*;

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

    public WatermarkPDFServiceImpl(PDDocument pdfDoc, Executor executor) {
        var watermarkPdfService = WatermarkPdfServiceBuilder.build(executor);
        currentWatermark = new WatermarkAttributes();
        watermarkHandler = (watermarks) -> watermarkPdfService.watermark(pdfDoc, watermarks);
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
}
