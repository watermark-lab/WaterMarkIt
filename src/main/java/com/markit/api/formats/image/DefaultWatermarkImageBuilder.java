package com.markit.api.formats.image;

import com.markit.api.WatermarkProcessor;
import com.markit.api.builders.DefaultWatermarkBuilder;
import com.markit.api.ImageType;
import com.markit.api.formats.image.WatermarkImageService.*;
import com.markit.exceptions.ConvertBytesToBufferedImageException;
import com.markit.exceptions.WatermarkingException;
import com.markit.image.ImageWatermarker;
import com.markit.servicelocator.ServiceFactory;

import java.io.File;

/**
 * @author Oleg Cheban
 * @since 1.3.0
 */
public final class DefaultWatermarkImageBuilder
        extends DefaultWatermarkBuilder<WatermarkImageService, WatermarkImageBuilder>
        implements WatermarkImageService, WatermarkImageBuilder {

    public DefaultWatermarkImageBuilder(byte[] fileBytes, ImageType imageType) {
        super(create(fileBytes, imageType));
    }

    public DefaultWatermarkImageBuilder(File file, ImageType imageType) {
        super(create(file, imageType));
    }

    private  static WatermarkProcessor create(Object fileSource, ImageType imageType) {
        var imageWatermarker = (ImageWatermarker) ServiceFactory.getInstance().getService(ImageWatermarker.class);

        return watermarks -> {
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
