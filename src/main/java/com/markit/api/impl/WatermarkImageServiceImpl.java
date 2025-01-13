package com.markit.api.impl;

import com.markit.api.AbstractWatermarkService;
import com.markit.api.ImageType;
import com.markit.api.WatermarkAttributes;
import com.markit.api.WatermarkImageService;
import com.markit.image.DefaultImageWatermarker;
import com.markit.api.WatermarkImageService.*;

import java.io.File;

/**
 * @author Oleg Cheban
 * @since 1.3.0
 */
public class WatermarkImageServiceImpl
        extends AbstractWatermarkService<WatermarkImageService, WatermarkImageBuilder, TextBasedWatermarkBuilder, WatermarkPositionStepBuilder>
        implements WatermarkImageService, WatermarkImageBuilder, TextBasedWatermarkBuilder, WatermarkPositionStepBuilder {

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
}
