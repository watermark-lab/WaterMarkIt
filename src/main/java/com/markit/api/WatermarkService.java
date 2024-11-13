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
    interface WatermarkTextToFile {

        /**
         * Sets the source file to be watermarked using a byte array.
         *
         * @param fileBytes The byte array representing the source file.
         * @param fileType The type of file (e.g., PDF, Image).
         * @see FileType
         */
        TextWatermarker watermark(byte[] fileBytes, FileType fileType);

        /**
         * Sets the source file to be watermarked using a File object.
         *
         * @param file The file to be watermarked.
         * @param fileType The type of file (e.g., PDF, Image).
         */
        TextWatermarker watermark(java.io.File file, FileType fileType);

        /**
         * Sets the PDF document to be watermarked.
         *
         * @param document The PDF document to be watermarked.
         */
        TextWatermarker watermark(PDDocument document);
    }

    interface TextWatermarker {

        /**
         * Sets the text to be used as the watermark.
         *
         * @param text The text for the watermark.
         */
        TextWatermarker withText(String text);

        /**
         * Sets the size of the watermark text.
         *
         * @param size The font size for the watermark text.
         */
        TextWatermarker ofSize(int size);

        /**
         * Defines the method for adding a watermark (default is OVERLAY).
         *
         * @param watermarkMethod The method to use for watermarking.
         * @see WatermarkMethod
         */
        TextWatermarker usingMethod(WatermarkMethod watermarkMethod);

        /**
         * Defines the position of the watermark on the file.
         *
         * @param watermarkPosition The position to place the watermark (e.g., CENTER, CORNER).
         * @see WatermarkPosition
         */
        TextWatermarker atPosition(WatermarkPosition watermarkPosition);

        /**
         * Sets the color of the watermark.
         *
         * @param color The color for the watermark text.
         * @see Color
         */
        TextWatermarker inColor(Color color);

        /**
         * Sets the opacity of the watermark.
         *
         * @param opacity The opacity value, ranging from 0.0 (fully transparent) to 1.0 (fully opaque).
         */
        TextWatermarker withOpacity(float opacity);

        /**
         * Specifies the resolution for the watermark in DPI.
         *
         * @param dpi The resolution in DPI.
         */
        TextWatermarker withDpi(float dpi);

        /**
         * Adds a trademark symbol to the watermark.
         *
         */
        TextWatermarker withTrademark();

        /**
         * Changes the rotation of the watermark
         */
        TextWatermarker rotate(int degree);

        /**
         * Adds another watermark configuration to the file.
         *
         * @return A new instance of Watermark for configuring another watermark.
         */
        TextWatermarker and();

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
    static WatermarkTextToFile createTextWatermarker() {
        return new TextWatermarkerServiceImpl();
    }

    /**
     * Creates a new instance of {@code WatermarkService} using the provided {@code Executor}.
     *
     * @param executor The executor for handling asynchronous operations.
     * @return A new instance of {@code WatermarkService}.
     * @throws NullPointerException If {@code executor} is null.
     */
    static WatermarkTextToFile createTextWatermarker(Executor executor) {
        Objects.requireNonNull(executor, "executor is required");
        return new TextWatermarkerServiceImpl(executor);
    }

    interface WatermarkImageToFile {

        /**
         * Sets the source file to be watermarked using a byte array.
         *
         * @param fileBytes The byte array representing the source file.
         * @param fileType The type of file (e.g., PDF, Image).
         * @see FileType
         */
        ImageWatermarker watermark(byte[] fileBytes, FileType fileType);

        /**
         * Sets the source file to be watermarked using a File object.
         *
         * @param file The file to be watermarked.
         * @param fileType The type of file (e.g., PDF, Image).
         */
        ImageWatermarker watermark(java.io.File file, FileType fileType);

        /**
         * Sets the PDF document to be watermarked.
         *
         * @param document The PDF document to be watermarked.
         */
        ImageWatermarker watermark(PDDocument document);
    }

    interface ImageWatermarker {
        ImageWatermarker ofSize(int size);
        ImageWatermarker withOpacity(float opacity);
        ImageWatermarker withDpi(float dpi);
        ImageWatermarker and();
        byte[] apply();
    }

    static WatermarkImageToFile createImageWatermarker() {
        return new ImageWatermarkerServiceImpl();
    }

    static WatermarkImageToFile createImageWatermarker(Executor executor) {
        Objects.requireNonNull(executor, "executor is required");
        return new ImageWatermarkerServiceImpl(executor);
    }
}
