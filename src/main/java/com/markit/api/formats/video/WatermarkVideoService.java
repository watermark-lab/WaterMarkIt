package com.markit.api.formats.video;

import com.markit.api.builders.TextBasedWatermarkBuilder;
import com.markit.api.builders.WatermarkBuilder;

import java.io.File;

/**
 * The Watermark Service for applying watermarks to videos
 */
public interface WatermarkVideoService {

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

    /**
     * The videos watermarking builder
     */
    interface WatermarkVideoBuilder extends WatermarkBuilder<WatermarkVideoService, WatermarkVideoBuilder> {}
}


