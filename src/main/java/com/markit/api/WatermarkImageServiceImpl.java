package com.markit.api;

import com.markit.exceptions.EmptyWatermarkObjectException;
import com.markit.exceptions.WatermarkingException;
import com.markit.image.DefaultImageWatermarker;
import com.markit.image.ImageConverter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WatermarkImageServiceImpl extends AbstractWatermarkService
        implements WatermarkImageService, WatermarkImageService.WatermarkImageBuilder, WatermarkImageService.TextBasedImageWatermarkBuilder, WatermarkImageService.WatermarkPositionStepBuilder {
    private static final Log logger = LogFactory.getLog(WatermarkImageServiceImpl.class);
    private final WatermarkHandler watermarkHandler;
    private final List<WatermarkAttributes> watermarks = new ArrayList<>();
    private WatermarkAttributes currentWatermark;

    public WatermarkImageServiceImpl(byte[] fileBytes, ImageType imageType) {
        this.currentWatermark = new WatermarkAttributes();
        var imageWatermarker = new DefaultImageWatermarker();
        this.watermarkHandler = (watermarks) -> imageWatermarker.watermark(fileBytes, imageType, watermarks);
    }

    public WatermarkImageServiceImpl(File file, ImageType imageType) {
        this.currentWatermark = new WatermarkAttributes();
        var imageWatermarker = new DefaultImageWatermarker();
        this.watermarkHandler = (watermarks) -> imageWatermarker.watermark(file, imageType, watermarks);
    }

    @Override
    public TextBasedImageWatermarkBuilder withText(String text) {
        currentWatermark.setText(text);
        return this;
    }

    @Override
    public WatermarkImageBuilder withImage(byte[] image) {
        var imageConverter = new ImageConverter();
        currentWatermark.setImage(Optional.of(imageConverter.convertToBufferedImage(image)));
        return this;
    }

    @Override
    public WatermarkImageService.WatermarkImageBuilder size(int size) {
        currentWatermark.setSize(size);
        return this;
    }

    @Override
    public WatermarkImageService.WatermarkImageBuilder opacity(float opacity) {
        currentWatermark.setOpacity(opacity);
        return this;
    }

    @Override
    public WatermarkImageService.WatermarkImageBuilder rotation(int degree) {
        currentWatermark.setRotation(degree);
        return this;
    }

    @Override
    public WatermarkImageService.WatermarkPositionStepBuilder position(WatermarkPosition watermarkPosition) {
        currentWatermark.setPosition(watermarkPosition);
        return this;
    }

    @Override
    public WatermarkImageService.WatermarkImageBuilder when(boolean condition) {
        currentWatermark.setWatermarkEnabled(condition);
        return this;
    }

    @Override
    public TextBasedImageWatermarkBuilder color(Color color) {
        currentWatermark.setColor(color);
        return this;
    }

    @Override
    public TextBasedImageWatermarkBuilder addTrademark() {
        currentWatermark.setTrademark(true);
        return this;
    }

    @Override
    public WatermarkImageService.WatermarkImageBuilder watermark() {
        return this;
    }

    @Override
    public WatermarkImageService.WatermarkImageBuilder adjust(int x, int y) {
        var adjustment = new WatermarkPositionCoordinates.Coordinates(x, y);
        currentWatermark.setPositionAdjustment(adjustment);
        return this;
    }

    @Override
    public WatermarkImageService and() {
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
