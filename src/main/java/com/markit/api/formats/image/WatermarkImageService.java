package com.markit.api.formats.image;

import com.markit.api.builders.TextBasedWatermarkBuilder;
import com.markit.api.builders.WatermarkBuilder;

import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Watermark Service for applying watermarks to images
 *
 * @author Oleg Cheban
 * @since 1.3.0
 */
public interface WatermarkImageService {

    /**
     * Sets the text to be used as the watermark
     *
     * @param text The text for the watermark
     */
    TextBasedWatermarkBuilder<WatermarkImageBuilder> withText(String text);

    /**
     * Sets the image to be used as the watermark
     * @param image the byte array representation of the image
     */
    WatermarkImageBuilder withImage(byte[] image);

    /**
     * Sets the image to be used as the watermark
     * @param image the BufferedImage representation of the image
     * @see BufferedImage
     */
    WatermarkImageBuilder withImage(BufferedImage image);

    /**
     * Sets the image to be used as the watermark
     * @param image the File object representing the image
     */
    WatermarkImageBuilder withImage(File image);

    /**
     * The general image watermarks builder
     */
    interface WatermarkImageBuilder extends WatermarkBuilder<WatermarkImageService, WatermarkImageBuilder> {}
}
