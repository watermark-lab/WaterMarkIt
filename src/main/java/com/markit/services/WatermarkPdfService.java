package com.markit.services;

import com.markit.services.impl.WatermarkMethod;
import com.markit.services.impl.WatermarkPosition;

import java.awt.*;
import java.io.IOException;

/**
 * to watermark pdf file
 *
 * @author Oleg Cheban
 * @since 1.0
 */
public interface WatermarkPdfService {
    /**
     * Adds a text watermark to a PDF file.
     *
     * @param sourceImageBytes The byte array representing the PDF file to which the watermark will be applied.
     * @param isAsyncMode      If {@code true}, the watermarking process will be performed asynchronously.
     *                         If {@code false}, it will be performed synchronously.
     * @param text    The text to be used as the watermark in the PDF file.
     * @param textSize          The size of watermark text.
     * @param color   The color of the watermark.
     * @param dpi              The resolution in dots per inch (DPI) at which the watermark will be applied.
     * @param trademark        Add a trademark symbol
     * @param method           The method for adding a watermark.
     * @param position         The position of watermark
     * @return A byte array representing the watermarked PDF file.
     */
    byte[] watermark(
            byte[] sourceImageBytes,
            boolean isAsyncMode,
            String text,
            int textSize,
            Color color,
            float dpi,
            boolean trademark,
            WatermarkMethod method,
            WatermarkPosition position) throws IOException;
}
