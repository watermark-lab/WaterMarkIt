package com.markit.services;

import com.markit.services.impl.FileType;

import java.awt.*;
import java.io.IOException;

/**
 * Interface for adding watermarks to images.
 *
 * @author Oleg Cheban
 * @since 1.0
 */
public interface ImageWatermarker {

    /**
     * Adds a text watermark to the given image.
     *
     * @param sourceImageBytes The image in byte array format to which the watermark will be applied.
     * @param watermarkText    The text to be used as the watermark.
     * @param watermarkColor   The color of the watermark.
     * @param trademark        Add a trademark symbol.
     * @return A byte array representing the watermarked image.
     */
    byte[] watermark(byte[] sourceImageBytes, FileType fileType, String watermarkText, Color watermarkColor, Boolean trademark) throws IOException;
}
