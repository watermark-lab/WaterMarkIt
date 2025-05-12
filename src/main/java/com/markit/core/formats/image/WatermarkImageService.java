package com.markit.core.formats.image;

import com.markit.core.builders.TextBasedWatermarkBuilder;
import com.markit.core.builders.WatermarkBuilder;

import java.awt.image.BufferedImage;
import java.io.File;

/**
 * The Watermark Service for applying watermarks to images
 *
 * @author Oleg Cheban
 * @since 1.3.0
 */
public interface WatermarkImageService {

    /**
     * Text-based watermarking method
     *
     * @param text The text for the watermark
     */
    TextBasedWatermarkBuilder<WatermarkImageBuilder> withText(String text);

    /**
     * Image-based watermarking method
     *
     * @param image the Byte array representation of the image
     */
    WatermarkImageBuilder withImage(byte[] image);

    /**
     * Image-based watermarking method
     *
     * @param image the BufferedImage representation of the image
     */
    WatermarkImageBuilder withImage(BufferedImage image);

    /**
     * Image-based watermarking method
     *
     * @param image the File object representing the image
     */
    WatermarkImageBuilder withImage(File image);

    /**
     * The images watermarking builder
     */
    interface WatermarkImageBuilder extends WatermarkBuilder<WatermarkImageService, WatermarkImageBuilder> {}
}
