package com.markit.api;

import com.markit.image.ImageConverter;
import com.markit.pdf.WatermarkPdfServiceBuilder;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.awt.*;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.function.Predicate;

public class WatermarkPDFServiceImpl extends AbstractWatermarkService<WatermarkPDFService>
        implements WatermarkPDFService, WatermarkPDFService.TextBasedPDFWatermarkBuilder, WatermarkPDFService.WatermarkPDFBuilder, WatermarkPDFService.WatermarkPositionStepPDFBuilder {

    public WatermarkPDFServiceImpl(PDDocument pdfDoc, Executor executor) {
        var watermarkPdfService = WatermarkPdfServiceBuilder.build(executor);
        currentWatermark = new WatermarkAttributes();
        watermarkHandler = (watermarks) -> watermarkPdfService.watermark(pdfDoc, watermarks);
    }

    @Override
    public TextBasedPDFWatermarkBuilder withText(String text) {
        currentWatermark.setText(text);
        return this;
    }

    @Override
    public WatermarkPDFBuilder withImage(byte[] image) {
        var imageConverter = new ImageConverter();
        currentWatermark.setImage(Optional.of(imageConverter.convertToBufferedImage(image)));
        return this;
    }

    @Override
    public WatermarkPDFBuilder method(WatermarkingMethod watermarkingMethod) {
        currentWatermark.setMethod(watermarkingMethod);
        return this;
    }

    @Override
    public WatermarkPDFBuilder size(int size) {
        currentWatermark.setSize(size);
        return this;
    }

    @Override
    public WatermarkPDFBuilder opacity(float opacity) {
        currentWatermark.setOpacity(opacity);
        return this;
    }

    @Override
    public WatermarkPDFBuilder rotation(int degree) {
        currentWatermark.setRotation(degree);
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
    public WatermarkPDFBuilder when(boolean condition) {
        currentWatermark.setWatermarkEnabled(condition);
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

    @Override
    public TextBasedPDFWatermarkBuilder color(Color color) {
        currentWatermark.setColor(color);
        return this;
    }

    @Override
    public TextBasedPDFWatermarkBuilder addTrademark() {
        currentWatermark.setTrademark(true);
        return this;
    }

    @Override
    public WatermarkPDFBuilder watermark() {
        return this;
    }

    @Override
    public WatermarkPDFBuilder adjust(int x, int y) {
        var adjustment = new WatermarkPositionCoordinates.Coordinates(x, y);
        currentWatermark.setPositionAdjustment(adjustment);
        return this;
    }
}
