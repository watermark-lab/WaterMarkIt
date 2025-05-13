package com.markit.core.formats.image;

import com.markit.core.WatermarkHandler;
import com.markit.core.builders.DefaultWatermarkBuilder;
import com.markit.core.ImageType;
import com.markit.core.formats.image.WatermarkImageService.*;
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
        super(createHandler(fileBytes, imageType));
    }

    public DefaultWatermarkImageBuilder(File file, ImageType imageType) {
        super(createHandler(file, imageType));
    }

    private  static WatermarkHandler createHandler(Object fileSource, ImageType imageType) {
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
