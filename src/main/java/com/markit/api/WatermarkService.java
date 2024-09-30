package com.markit.api;

import com.markit.image.ImageWatermarker;
import com.markit.pdf.OverlayPdfWatermarker;
import com.markit.pdf.PdfWatermarker;
import com.markit.pdf.WatermarkPdfService;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.awt.*;
import java.util.Objects;
import java.util.concurrent.Executor;

/**
 * Watermark Service for applying watermarks to different file types.
 *
 * @author Oleg Cheban
 * @since 1.0
 */
public interface WatermarkService {
    interface File {

        /**
         * Sets the source file to be watermarked using a byte array.
         *
         * @param fileBytes The byte array representing the source file.
         * @param fileType The type of file (e.g., PDF, Image).
         * @see FileType
         */
        Watermark file(byte[] fileBytes, FileType fileType);

        /**
         * Sets the source file to be watermarked using a File object.
         *
         * @param file The file to be watermarked.
         * @param fileType The type of file (e.g., PDF, Image).
         */
        Watermark file(java.io.File file, FileType fileType);

        /**
         * Sets the PDF document to be watermarked.
         *
         * @param document The PDF document to be watermarked.
         */
        Watermark file(PDDocument document);
    }

    interface Watermark {

        /**
         * Sets the text to be used as the watermark.
         *
         * @param text The text for the watermark.
         */
        Watermark text(String text);

        /**
         * Sets the size of the watermark text.
         *
         * @param size The font size for the watermark text.
         */
        Watermark textSize(int size);

        /**
         * Defines the method for adding a watermark (default is OVERLAY).
         *
         * @param watermarkMethod The method to use for watermarking.
         * @see WatermarkMethod
         */
        Watermark method(WatermarkMethod watermarkMethod);

        /**
         * Defines the position of the watermark on the file.
         *
         * @param watermarkPosition The position to place the watermark (e.g., CENTER, CORNER).
         * @see WatermarkPosition
         */
        Watermark position(WatermarkPosition watermarkPosition);

        /**
         * Sets the color of the watermark.
         *
         * @param color The color for the watermark text.
         * @see Color
         */
        Watermark color(Color color);

        /**
         * Specifies the resolution for the watermark in DPI.
         *
         * @param dpi The resolution in DPI.
         */
        Watermark dpi(float dpi);

        /**
         * Adds a trademark symbol to the watermark.
         *
         * @return The current instance with the trademark added.
         */
        Watermark trademark();

        /**
         * Enables synchronous mode for applying the watermark.
         *
         * @return The current instance with sync mode enabled.
         */
        Watermark sync();

        /**
         * Applies the watermark to the file and returns the result as a byte array.
         *
         * @return A byte array representing the watermarked file.
         */
        byte[] apply();
    }

    /**
     * Creates a new instance of {@code WatermarkService}.
     *
     * @return A new instance of the {@code WatermarkService}.
     */
    static File create() {
        return new WatermarkServiceImpl();
    }

    /**
     * Creates a new instance of {@code WatermarkService} using the provided {@code Executor}.
     *
     * @param executor The executor for handling asynchronous operations.
     * @return A new instance of {@code WatermarkService}.
     * @throws NullPointerException If {@code executor} is null.
     */
    static File create(Executor executor) {
        Objects.requireNonNull(executor, "executor is required");
        return new WatermarkServiceImpl(executor);
    }

    /**
     * Creates a new instance of {@code WatermarkService} with the specified components.
     *
     * @param exr The executor for asynchronous tasks.
     * @param i The image watermarker.
     * @param d The PDF watermarker.
     * @param o The overlay PDF watermarker.
     * @param s The watermark PDF service.
     * @return A new instance of {@code WatermarkService}.
     */
    static File create(Executor exr, ImageWatermarker i, PdfWatermarker d, OverlayPdfWatermarker o, WatermarkPdfService s) {
        nullCheck(i, d, o, s);
        Objects.requireNonNull(exr, "Executor is required");
        return new WatermarkServiceImpl(exr, i, d, o, s);
    }

    /**
     * Creates a new instance of {@code WatermarkService} with the specified components.
     *
     * @param i The image watermarker.
     * @param d The PDF watermarker.
     * @param o The overlay PDF watermarker.
     * @param s The watermark PDF service.
     * @return A new instance of {@code WatermarkService}.
     */
    static File create(ImageWatermarker i, PdfWatermarker d, OverlayPdfWatermarker o, WatermarkPdfService s) {
        nullCheck(i, d, o, s);
        return new WatermarkServiceImpl(i, d, o, s);
    }

    static void nullCheck(ImageWatermarker i, PdfWatermarker d, OverlayPdfWatermarker o, WatermarkPdfService s){
        Objects.requireNonNull(i, "ImageWatermarker is required");
        Objects.requireNonNull(d, "PdfWatermarkDrawService is required");
        Objects.requireNonNull(o, "PdfWatermarkOverlayService is required");
        Objects.requireNonNull(s, "WatermarkPdfService is required");
    }
}
