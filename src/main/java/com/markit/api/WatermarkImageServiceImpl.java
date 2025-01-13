package com.markit.api;

import com.markit.image.DefaultImageWatermarker;
import com.markit.image.ImageConverter;

import java.awt.*;
import java.io.File;
import java.util.Optional;

public class WatermarkImageServiceImpl extends AbstractWatermarkService<WatermarkImageService, WatermarkImageService.WatermarkImageBuilder>
        implements WatermarkImageService, WatermarkImageService.WatermarkImageBuilder, WatermarkImageService.TextBasedImageWatermarkBuilder, WatermarkImageService.WatermarkPositionStepBuilder {

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
    public WatermarkImageService.WatermarkPositionStepBuilder position(WatermarkPosition watermarkPosition) {
        currentWatermark.setPosition(watermarkPosition);
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
}
