package com.markit.api.builders;

import java.awt.*;

/**
 * Text-based watermarks builder
 *
 * @author Oleg Cheban
 * @since 1.3.3
 */
public interface TextBasedWatermarkBuilder<WatermarkBuilderType> {
    /**
     * Sets the color of the text
     *
     * @param color The color for the text
     * @see Color
     */
    TextBasedWatermarkBuilder<WatermarkBuilderType> color(Color color);

    /**
     * Adds a trademark symbol to the text
     */
    TextBasedWatermarkBuilder<WatermarkBuilderType> addTrademark();

    /**
     * Finish working with TextBasedWatermarkBuilder and back to the WatermarkBuilderType
     */
    WatermarkBuilderType end();
}
