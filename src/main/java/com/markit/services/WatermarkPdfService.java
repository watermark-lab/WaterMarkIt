package com.markit.services;

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
     * @param watermarkText    The text to be used as the watermark in the PDF file.
     * @param watermarkColor   The color of the watermark.
     * @param dpi              The resolution in dots per inch (DPI) at which the watermark will be applied.
     * @param trademark        Add a trademark symbol
     * @return A byte array representing the watermarked PDF file.
     */
    byte[] watermark(byte[] sourceImageBytes, Boolean isAsyncMode, String watermarkText, Color watermarkColor, float dpi, Boolean trademark) throws IOException;
}
