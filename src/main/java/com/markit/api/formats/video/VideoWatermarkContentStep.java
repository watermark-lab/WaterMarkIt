package com.markit.api.formats.video;

import com.markit.api.builders.TextBasedWatermarkBuilder;

import java.io.File;

/**
 * Content-selection stage for configuring video watermarks.
 *
 * @since 1.4.0
 */
public interface VideoWatermarkContentStep {

    /**
     * Text-based watermarking method
     *
     * @param text The text for the watermark
     */
    TextBasedWatermarkBuilder<WatermarkVideoBuilder> withText(String text);

    /**
     * Image-based watermarking method
    */
    WatermarkVideoBuilder withImage(byte[] image);

    /**
     * Image-based watermarking method
     */
    WatermarkVideoBuilder withImage(java.awt.image.BufferedImage image);

    /**
     * Image-based watermarking method
     */
    WatermarkVideoBuilder withImage(File image);
}


