package com.markit.api;

import com.markit.image.ImageConverter;
import com.markit.pdf.WatermarkPdfServiceBuilder;
import org.apache.pdfbox.pdmodel.PDDocument;
import com.markit.api.WatermarkPDFService.*;

import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.function.Predicate;

public class WatermarkPDFServiceImpl extends AbstractWatermarkService<WatermarkPDFService, WatermarkPDFBuilder, TextBasedWatermarkBuilder>
        implements WatermarkPDFService, TextBasedWatermarkBuilder, WatermarkPDFBuilder, WatermarkPositionStepPDFBuilder {

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
    public WatermarkPositionStepPDFBuilder position(WatermarkPosition watermarkPosition) {
        currentWatermark.setPosition(watermarkPosition);
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
