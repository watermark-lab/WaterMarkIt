package com.markit.image;

import com.markit.api.WatermarkPosition;
import com.markit.api.WatermarkPositionCoordinates;
import com.markit.pdf.draw.DrawMethodPositionCoordinates;

import java.util.List;

/**
 * @author Oleg Cheban
 * @since 1.0
 */
public class WatermarkPositioner {
    public List<WatermarkPositionCoordinates.Coordinates> defineXY(
            WatermarkPosition position,
            int imageWidth,
            int imageHeight,
            int watermarkWidth,
            int watermarkHeight) {
        return new DrawMethodPositionCoordinates(
                imageWidth,
                imageHeight,
                watermarkWidth,
                watermarkHeight).getCoordinatesForPosition(position);
    }
}
