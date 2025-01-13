package com.markit.api;

import com.markit.exceptions.EmptyWatermarkObjectException;
import com.markit.exceptions.WatermarkingException;
import com.markit.image.ImageConverter;
import com.markit.pdf.WatermarkPdfServiceBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.function.Predicate;

public class WatermarkPDFServiceImpl extends AbstractWatermarkService
        implements WatermarkPDFService, WatermarkPDFService.TextBasedPDFWatermarkBuilder, WatermarkPDFService.WatermarkPDFBuilder, WatermarkPDFService.WatermarkPositionStepPDFBuilder {
    private static final Log logger = LogFactory.getLog(WatermarkPDFServiceImpl.class);
    private final WatermarkHandler watermarkHandler;
    private final List<WatermarkAttributes> watermarks = new ArrayList<>();
    private WatermarkAttributes currentWatermark;

    public WatermarkPDFServiceImpl(PDDocument pdfDoc, Executor executor) {
        var watermarkPdfService = WatermarkPdfServiceBuilder.build(executor);
        currentWatermark = new WatermarkAttributes();
        this.watermarkHandler = (watermarks) -> watermarkPdfService.watermark(pdfDoc, watermarks);
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

    @Override
    public WatermarkPDFService and() {
        if (currentWatermark.getText().isEmpty() && currentWatermark.getImage().isEmpty()) {
            logger.error("the watermark content is empty");
            throw new EmptyWatermarkObjectException();
        }
        watermarks.add(currentWatermark);
        currentWatermark = new WatermarkAttributes();
        return this;
    }

    @NotNull
    @Override
    public byte[] apply() {
        try {
            and();
            return this.watermarkHandler.apply(this.watermarks);
        } catch (IOException e) {
            logger.error("Failed to watermark file", e);
            throw new WatermarkingException("Error watermarking the file", e);
        }
    }
}
