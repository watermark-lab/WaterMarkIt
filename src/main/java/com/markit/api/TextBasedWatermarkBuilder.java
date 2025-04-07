package com.markit.api;

import java.awt.*;

/**
 * Text-based watermarks builder
 *
 * @author Oleg Cheban
 * @since 1.3.3
 */
public interface TextBasedWatermarkBuilder<T> {
    /**
     * Sets the color of the text
     *
     * @param color The color for the text
     * @see Color
     */
    TextBasedWatermarkBuilder<T> color(Color color);

    /**
     * Adds a trademark symbol to the text
     */
    TextBasedWatermarkBuilder<T> addTrademark();

    /**
     * Getting watermarks builder
     */
    T end();

    /**
     * Applies the watermark to the file and returns the result as a byte array
     *
     * @return A byte array representing the watermarked file
     */
    byte[] apply();
}
