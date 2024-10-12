package com.markit.pdf;

import com.markit.api.WatermarkPosition;
import com.markit.api.WatermarkPositionCoordinates;

/**
 * @author Oleg Cheban
 * @since 1.0
 */
public class WatermarkPositioner {
    public WatermarkPositionCoordinates.Coordinates defineXY(
            WatermarkPosition position,
            int imageWidth,
            int imageHeight,
            int watermarkWidth,
            int watermarkHeight) {
        return new OverlayMethodPositionCoordinates(
                imageWidth,
                imageHeight,
                watermarkWidth,
                watermarkHeight).getCoordinatesForPosition(position);
    }
}
