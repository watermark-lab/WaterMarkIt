package com.markit.image;

import com.markit.api.FileType;
import com.markit.api.WatermarkPosition;

import java.awt.*;
import java.io.File;
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
     * @param sourceImageBytes The image in byte array format.
     * @param text The text to be used as the watermark.
     * @param color The color of the watermark.
     * @param trademark Add a trademark symbol.
     * @param position Position of watermark.
     * @return A byte array representing the watermarked image.
     */
    byte[] watermark(
            byte[] sourceImageBytes,
            FileType fileType,
            String text,
            int textSize,
            Color color,
            boolean trademark,
            WatermarkPosition position) throws IOException;

    /**
     * Adds a text watermark to the given image.
     *
     * @param file The source file of image.
     * @param text The text to be used as the watermark.
     * @param color The color of the watermark.
     * @param trademark Add a trademark symbol.
     * @param position Position of watermark.
     * @return A byte array representing the watermarked image.
     */
    byte[] watermark(
            File file,
            FileType fileType,
            String text,
            int textSize,
            Color color,
            boolean trademark,
            WatermarkPosition position) throws IOException;
}
