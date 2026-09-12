package com.markit.api.formats.pdf;

import com.markit.api.builders.TextBasedWatermarkBuilder;

import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Content-selection stage for configuring PDF watermarks.
 *
 * @author Oleg Cheban
 * @since 1.3.0
 */
public interface PdfWatermarkContentStep {

    /**
     * Text-based watermarking method
     *
     * @param text The text for the watermark
     */
    TextBasedWatermarkBuilder<WatermarkPDFBuilder> withText(String text);

    /**
     * Image-based watermarking method
     *
     * @param image the Byte array representation of the image
     */
    WatermarkPDFBuilder withImage(byte[] image);

    /**
     * Image-based watermarking method
     *
     * @param image the BufferedImage representation of the image
     */
    WatermarkPDFBuilder withImage(BufferedImage image);

    /**
     * Image-based watermarking method
     *
     * @param image the File object representing the image
     */
    WatermarkPDFBuilder withImage(File image);
}
