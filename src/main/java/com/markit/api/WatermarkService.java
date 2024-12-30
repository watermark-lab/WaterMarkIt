package com.markit.api;

import org.apache.pdfbox.pdmodel.PDDocument;

import java.awt.*;
import java.io.File;
import java.util.Objects;
import java.util.concurrent.Executor;

/**
 * Watermark Service for applying watermarks to different file types.
 *
 * @author Oleg Cheban
 * @since 1.0
 */
public interface WatermarkService {

    static TextBasedWatermarker textBasedWatermarker() {
        return new TextBasedWatermarkServiceImpl();
    }

    static TextBasedWatermarker textBasedWatermarker(Executor executor) {
        Objects.requireNonNull(executor, "executor is required");
        return new TextBasedWatermarkServiceImpl(executor);
    }

    static ImageBasedWatermarker imageBasedWatermarker() {
        return new ImageBasedWatermarkServiceImpl();
    }

    static ImageBasedWatermarker imageBasedWatermarker(Executor executor) {
        Objects.requireNonNull(executor, "executor is required");
        return new ImageBasedWatermarkServiceImpl(executor);
    }

    /**
     * text-based watermarks service
     */
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
        TextBasedWatermarkBuilder watermark(File file, FileType fileType);

        /**
         * Sets the PDF document to be watermarked.
         *
         * @param document The PDF document to be watermarked.
         */
        TextBasedWatermarkBuilder watermark(PDDocument document);
    }

    /**
     * interface for building and applying text-based watermarks
     */
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
        TextBasedWatermarkBuilder size(int size);

        /**
         * Defines the method for adding a watermark (default is OVERLAY).
         *
         * @param watermarkingMethod The method to use for watermarking.
         * @see WatermarkingMethod
         */
        TextBasedWatermarkBuilder method(WatermarkingMethod watermarkingMethod);

        /**
         * Defines the position of the watermark on the file.
         *
         * @param watermarkPosition The position to place the watermark (e.g., CENTER, CORNER).
         * @see WatermarkPosition
         */
        TextBasedWatermarkPositionStepBuilder position(WatermarkPosition watermarkPosition);

        /**
         * Sets the color of the watermark.
         *
         * @param color The color for the watermark text.
         * @see Color
         */
        TextBasedWatermarkBuilder color(Color color);

        /**
         * Sets the opacity of the watermark.
         *
         * @param opacity The opacity value, ranging from 0.0 (fully transparent) to 1.0 (fully opaque).
         */
        TextBasedWatermarkBuilder opacity(float opacity);

        /**
         * Specifies the resolution for the watermark in DPI.
         *
         * @param dpi The resolution in DPI.
         */
        TextBasedWatermarkBuilder dpi(float dpi);

        /**
         * Adds a trademark symbol to the watermark.
         *
         */
        TextBasedWatermarkBuilder addTrademark();

        /**
         * Changes the rotation of the watermark
         */
        TextBasedWatermarkBuilder rotation(int degree);

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
     * Interface for adjusting the position of text-based watermarks.
     */
    interface TextBasedWatermarkPositionStepBuilder extends TextBasedWatermarkBuilder {

        /**
         * Adjusts the position of the watermark relative to its default location.
         *
         * @param x The horizontal offset in pixels.
         * @param y The vertical offset in pixels.
         */
        TextBasedWatermarkBuilder adjust(int x, int y);
    }

    /**
     * image-based watermarks service
     */
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
        ImageBasedWatermarkBuilder watermark(File file, FileType fileType);

        /**
         * Sets the PDF document to be watermarked.
         *
         * @param document The PDF document to be watermarked.
         */
        ImageBasedWatermarkBuilder watermark(PDDocument document);
    }

    /**
     * interface for building and applying image-based watermarks
     */
    interface ImageBasedWatermarkBuilder {
        /**
         * Sets the watermark image.
         */
        ImageBasedWatermarkBuilder withImage(byte[] image);

        /**
         * Sets the size of the watermark image.
         */
        ImageBasedWatermarkBuilder size(int size);
        /**
         * Sets the opacity of the watermark.
         */
        ImageBasedWatermarkBuilder opacity(float opacity);

        /**
         * Sets the rotation of the watermark.
         */
        ImageBasedWatermarkBuilder rotation(int degree);
        /**
         * Sets the dpi of the watermark.
         */
        ImageBasedWatermarkBuilder dpi(float dpi);
        /**
         * Defines the position of the watermark on the file.
         *
         * @param position The position to place the watermark
         * @see WatermarkPosition
         */
        ImageBasedWatermarkPositionStepBuilder position(WatermarkPosition position);

        /**
         * Applies the watermark to the file and returns the result as a byte array.
         *
         * @return A byte array representing the watermarked file.
         */
        byte[] apply();
    }

    /**
     * Interface for adjusting the position of image-based watermarks.
     */
    interface ImageBasedWatermarkPositionStepBuilder extends ImageBasedWatermarkBuilder {

        /**
         * Adjusts the position of the watermark relative to its default location.
         *
         * @param x The horizontal offset in pixels.
         * @param y The vertical offset in pixels.
         */
        ImageBasedWatermarkBuilder adjust(int x, int y);
    }
}
