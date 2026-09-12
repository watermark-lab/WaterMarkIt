package com.markit.api.formats.image;

import com.markit.api.builders.TextBasedWatermarkBuilder;

import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Content-selection stage for configuring image watermarks.
 *
 * @author Oleg Cheban
 * @since 1.3.0
 */
public interface ImageWatermarkContentStep {

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
}
