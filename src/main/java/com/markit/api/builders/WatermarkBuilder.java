package com.markit.api.builders;

import com.markit.api.positioning.WatermarkPosition;

/**
 * Watermark Builder
 *
 * @author Oleg Cheban
 * @since 1.3.3
 */
public interface WatermarkBuilder<WatermarkServiceType, WatermarkBuilderType> {

    /**
     * Sets the size of the watermark
     */
    WatermarkBuilderType size(int size);

    /**
     * Sets the opacity of the watermark
     */
    WatermarkBuilderType opacity(int opacity);

    /**
     * Sets the rotation of the watermark
     */
    WatermarkBuilderType rotation(int degree);

    /**
     * Defines the position of the watermark on the file
     *
     * @param watermarkPosition The position to place the watermark (e.g., CENTER, TILED).
     * @see WatermarkPosition
     */
    PositionStepBuilder<WatermarkBuilderType> position(WatermarkPosition watermarkPosition);

    /**
     * Enables or disables the watermark based on a specific condition
     *
     * @param condition: A boolean value that determines whether the watermark is enabled (true) or disabled (false)
     */
    WatermarkBuilderType enableIf(boolean condition);

    /**
     * Adds another watermark configuration to the file
     *
     * @return The watermark service for configuring another watermark
     */
    WatermarkServiceType and();

    /**
     * Applies the watermark to the file and returns the result as a byte array
     *
     * @return A byte array representing the watermarked file
     */
    byte[] apply();
}
