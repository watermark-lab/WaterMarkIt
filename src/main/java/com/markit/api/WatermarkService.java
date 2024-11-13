package com.markit.api;

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
    interface TextBasedWatermarker {

        /**
         * Sets the source file to be watermarked using a byte array.
         *
         * @param fileBytes The byte array representing the source file.
         * @param fileType The type of file (e.g., PDF, Image).
         * @see FileType
         */
        TextBasedWatermarkBuilder watermark(byte[] fileBytes, FileType fileType);

        /**
         * Sets the source file to be watermarked using a File object.
         *
         * @param file The file to be watermarked.
         * @param fileType The type of file (e.g., PDF, Image).
         */
        TextBasedWatermarkBuilder watermark(java.io.File file, FileType fileType);

        /**
         * Sets the PDF document to be watermarked.
         *
         * @param document The PDF document to be watermarked.
         */
        TextBasedWatermarkBuilder watermark(PDDocument document);
    }

    interface TextBasedWatermarkBuilder {

        /**
         * Sets the text to be used as the watermark.
         *
         * @param text The text for the watermark.
         */
        TextBasedWatermarkBuilder withText(String text);

        /**
         * Sets the size of the watermark text.
         *
         * @param size The font size for the watermark text.
         */
        TextBasedWatermarkBuilder ofSize(int size);

        /**
         * Defines the method for adding a watermark (default is OVERLAY).
         *
         * @param watermarkMethod The method to use for watermarking.
         * @see WatermarkMethod
         */
        TextBasedWatermarkBuilder usingMethod(WatermarkMethod watermarkMethod);

        /**
         * Defines the position of the watermark on the file.
         *
         * @param watermarkPosition The position to place the watermark (e.g., CENTER, CORNER).
         * @see WatermarkPosition
         */
        TextBasedWatermarkBuilder atPosition(WatermarkPosition watermarkPosition);

        /**
         * Sets the color of the watermark.
         *
         * @param color The color for the watermark text.
         * @see Color
         */
        TextBasedWatermarkBuilder inColor(Color color);

        /**
         * Sets the opacity of the watermark.
         *
         * @param opacity The opacity value, ranging from 0.0 (fully transparent) to 1.0 (fully opaque).
         */
        TextBasedWatermarkBuilder withOpacity(float opacity);

        /**
         * Specifies the resolution for the watermark in DPI.
         *
         * @param dpi The resolution in DPI.
         */
        TextBasedWatermarkBuilder withDpi(float dpi);

        /**
         * Adds a trademark symbol to the watermark.
         *
         */
        TextBasedWatermarkBuilder withTrademark();

        /**
         * Changes the rotation of the watermark
         */
        TextBasedWatermarkBuilder rotate(int degree);

        /**
         * Adds another watermark configuration to the file.
         *
         * @return A new instance of Watermark for configuring another watermark.
         */
        TextBasedWatermarkBuilder and();

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
    static TextBasedWatermarker createTextBasedWatermarker() {
        return new TextBasedWatermarkServiceImpl();
    }

    /**
     * Creates a new instance of {@code WatermarkService} using the provided {@code Executor}.
     *
     * @param executor The executor for handling asynchronous operations.
     * @return A new instance of {@code WatermarkService}.
     * @throws NullPointerException If {@code executor} is null.
     */
    static TextBasedWatermarker createTextBasedWatermarker(Executor executor) {
        Objects.requireNonNull(executor, "executor is required");
        return new TextBasedWatermarkServiceImpl(executor);
    }

    interface ImageBasedWatermarker {

        /**
         * Sets the source file to be watermarked using a byte array.
         *
         * @param fileBytes The byte array representing the source file.
         * @param fileType The type of file (e.g., PDF, Image).
         * @see FileType
         */
        ImageBasedWatermarkBuilder watermark(byte[] fileBytes, FileType fileType);

        /**
         * Sets the source file to be watermarked using a File object.
         *
         * @param file The file to be watermarked.
         * @param fileType The type of file (e.g., PDF, Image).
         */
        ImageBasedWatermarkBuilder watermark(java.io.File file, FileType fileType);

        /**
         * Sets the PDF document to be watermarked.
         *
         * @param document The PDF document to be watermarked.
         */
        ImageBasedWatermarkBuilder watermark(PDDocument document);
    }

    interface ImageBasedWatermarkBuilder {
        ImageBasedWatermarkBuilder ofSize(int size);
        ImageBasedWatermarkBuilder withOpacity(float opacity);
        ImageBasedWatermarkBuilder withDpi(float dpi);
        ImageBasedWatermarkBuilder and();
        byte[] apply();
    }

    static ImageBasedWatermarker createImageBasedWatermarker() {
        return new ImageBasedWatermarkServiceImpl();
    }

    static ImageBasedWatermarker createImageBasedWatermarker(Executor executor) {
        Objects.requireNonNull(executor, "executor is required");
        return new ImageBasedWatermarkServiceImpl(executor);
    }
}
