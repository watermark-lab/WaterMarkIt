package com.markit.api;

import java.awt.*;
import java.nio.file.Path;

/**
 * Watermark Service for applying watermarks to images
 *
 * @author Oleg Cheban
 * @since 1.3.0
 */
public interface WatermarkImageService {

    /**
     * Sets the text to be used as the watermark
     *
     * @param text The text for the watermark
     */
    TextBasedWatermarkBuilder withText(String text);

    /**
     * Sets the image to be used as the watermark
     */
    WatermarkImageBuilder withImage(byte[] image);

    /**
     * Text-based watermarks builder
     */
    interface TextBasedWatermarkBuilder {
        /**
         * Sets the color of the text
         *
         * @param color The color for the text
         * @see Color
         */
        TextBasedWatermarkBuilder color(Color color);

        /**
         * Adds a trademark symbol to the text
         */
        TextBasedWatermarkBuilder addTrademark();

        /**
         * Getting watermarks builder
         */
        WatermarkImageBuilder watermark();

        /**
         * Applies the watermark to the file and returns the result as a byte array
         *
         * @return A byte array representing the watermarked file
         */
        byte[] apply();
    }

    /**
     * The general image watermarks builder
     */
    interface WatermarkImageBuilder {

        /**
         * Sets the size of the watermark
         */
        WatermarkImageBuilder size(int size);

        /**
         * Sets the opacity of the watermark
         */
        WatermarkImageBuilder opacity(float opacity);

        /**
         * Sets the rotation of the watermark
         */
        WatermarkImageBuilder rotation(int degree);

        /**
         * Defines the position of the watermark on the file
         *
         * @param watermarkPosition The position to place the watermark (e.g., CENTER, CORNER)
         * @see WatermarkPosition
         */
        WatermarkPositionStepBuilder position(WatermarkPosition watermarkPosition);

        /**
         * Enables or disables the watermark based on a specific condition
         *
         * @param condition: A boolean value that determines whether the watermark is enabled (true) or disabled (false)
         */
        WatermarkImageBuilder when(boolean condition);

        /**
         * Adds another watermark configuration to the file.
         *
         * @return A new instance of Watermark for configuring another watermark.
         */
        WatermarkImageService and();

        /**
         * Applies the watermark to the file and returns the result as a byte array
         *
         * @return A byte array representing the watermarked file
         */
        byte[] apply();

        /**
         * Applies a watermark to the file and returns the result as a file path
         * The method generates a watermarked file saved in the specified directory
         * and file name, and provides the file's path for further processing
         *
         * @param directoryPath The directory path where the watermarked file will be saved
         * @param fileName      The name of the watermarked file to be created
         * @return The {@link Path} representing the location of the saved watermarked file
         */
        Path apply(String directoryPath, String fileName);
    }

    /**
     * Interface for adjusting the position of watermarks
     */
    interface WatermarkPositionStepBuilder extends WatermarkImageBuilder {
        /**
         * Adjusts the position of the watermark relative to its default location
         *
         * @param x The horizontal offset in pixels
         * @param y The vertical offset in pixels
         */
        WatermarkImageBuilder adjust(int x, int y);
    }
}
