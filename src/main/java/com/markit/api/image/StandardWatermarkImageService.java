package com.markit.api.image;

import com.markit.api.AbstractWatermarkService;
import com.markit.api.ImageType;
import com.markit.image.DefaultImageWatermarker;
import com.markit.api.image.WatermarkImageService.*;

import java.io.File;

/**
 * @author Oleg Cheban
 * @since 1.3.0
 */
public class StandardWatermarkImageService
        extends AbstractWatermarkService<WatermarkImageService, WatermarkImageBuilder, TextBasedWatermarkBuilder, WatermarkPositionStepBuilder>
        implements WatermarkImageService, WatermarkImageBuilder, TextBasedWatermarkBuilder, WatermarkPositionStepBuilder {

    public StandardWatermarkImageService(byte[] fileBytes, ImageType imageType) {
        initializeWatermarkHandler(fileBytes, imageType);
    }

    public StandardWatermarkImageService(File file, ImageType imageType) {
        initializeWatermarkHandler(file, imageType);
    }

    private void initializeWatermarkHandler(Object fileSource, ImageType imageType) {
        var imageWatermarker = new DefaultImageWatermarker();
        this.watermarkHandler = (watermarks) -> {
            if (fileSource instanceof byte[]) {
                return imageWatermarker.watermark((byte[]) fileSource, imageType, watermarks);
            } else if (fileSource instanceof File) {
                return imageWatermarker.watermark((File) fileSource, imageType, watermarks);
            } else {
                throw new IllegalArgumentException("Unsupported file source type: " + fileSource.getClass().getName());
            }
        };
    }
}
