package com.markit.api.formats.image;

import com.markit.api.AbstractWatermarkService;
import com.markit.api.ImageType;
import com.markit.api.formats.image.WatermarkImageService.*;
import com.markit.image.ImageWatermarker;
import com.markit.servicelocator.ServiceFactory;

import java.io.File;

/**
 * @author Oleg Cheban
 * @since 1.3.0
 */
@SuppressWarnings("unchecked")
public class DefaultWatermarkImageService
        extends AbstractWatermarkService<WatermarkImageService, WatermarkImageBuilder>
        implements WatermarkImageService, WatermarkImageBuilder {

    public DefaultWatermarkImageService(byte[] fileBytes, ImageType imageType) {
        initializeWatermarkHandler(fileBytes, imageType);
    }

    public DefaultWatermarkImageService(File file, ImageType imageType) {
        initializeWatermarkHandler(file, imageType);
    }

    private void initializeWatermarkHandler(Object fileSource, ImageType imageType) {
        var imageWatermarker = (ImageWatermarker) ServiceFactory.getInstance().getService(ImageWatermarker.class);

        this.watermarkHandler = (watermarks) -> {
            try {
                if (fileSource instanceof byte[]) {
                    return imageWatermarker.watermark((byte[]) fileSource, imageType, watermarks);
                } else if (fileSource instanceof File) {
                    return imageWatermarker.watermark((File) fileSource, imageType, watermarks);
                } else {
                    throw new IllegalArgumentException("Unsupported file source type: " + fileSource.getClass().getName());
                }
            } catch (ConvertBytesToBufferedImageException e) {
                throw new WatermarkingException("Error converting bytes to buffered image", e);
            }
        };
    }

}
