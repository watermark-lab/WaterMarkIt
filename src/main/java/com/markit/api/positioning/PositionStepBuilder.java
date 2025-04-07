package com.markit.api.positioning;

/**
 * Interface for adjusting the position of watermarks
 *
 * @author Oleg Cheban
 * @since 1.3.3
 */
public interface PositionStepBuilder<T> {

    /**
     * Adjusts the position of the watermark relative to its default location
     *
     * @param x The horizontal offset in pixels
     * @param y The vertical offset in pixels
     */
    PositionStepBuilder<T> adjust(int x, int y);

    /**
     * Sets the vertical spacing between multiple tiled watermarks on the page.
     * This is only relevant when the watermark is tiled.
     *
     * @param spacing The spacing between tiles in pixels along the vertical axis.
     *                A larger value increases the distance between adjacent watermarks vertically.
     * @return The current instance of {@code WatermarkPDFBuilder} for method chaining.
     */
    PositionStepBuilder<T> verticalSpacing(int spacing);

    /**
     * Sets the horizontal spacing between multiple tiled watermarks on the page.
     * This is only relevant when the watermark is tiled.
     *
     * @param spacing The spacing between tiles in pixels along the horizontal axis.
     *                A larger value increases the distance between adjacent watermarks horizontally.
     * @return The current instance of {@code WatermarkPDFBuilder} for method chaining.
     */
    PositionStepBuilder<T> horizontalSpacing(int spacing);

    /**
     * Finish working with WatermarkPositionStepPDFBuilder and back to WatermarkPDFBuilder
     */
    T end();
}
