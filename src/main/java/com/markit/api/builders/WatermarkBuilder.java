package com.markit.api.builders;

import com.markit.api.positioning.WatermarkPosition;

import java.nio.file.Path;

/**
 * Watermark Builder
 *
 * @author Oleg Cheban
 * @since 1.3.3
 */
public interface WatermarkBuilder<Service, Builder> {

        /**
         * Sets the size of the watermark
         */
        Builder size(int size);

        /**
         * Sets the opacity of the watermark
         */
        Builder opacity(int opacity);

        /**
         * Sets the rotation of the watermark
         */
        Builder rotation(int degree);

        /**
         * Defines the position of the watermark on the file
         *
         * @param watermarkPosition The position to place the watermark (e.g., CENTER, TILED).
         * @see WatermarkPosition
         */
        PositionStepBuilder<Builder> position(WatermarkPosition watermarkPosition);

        /**
         * Enables or disables the watermark based on a specific condition
         *
         * @param condition: A boolean value that determines whether the watermark is enabled (true) or disabled (false)
         */
        Builder when(boolean condition);

        /**
         * Adds another watermark configuration to the file
         *
         * @return The watermark service for configuring another watermark
         */
        Service and();

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
