package com.markit.image;

import com.markit.core.WatermarkAttributes;
import com.markit.core.positioning.WatermarkPositionCoordinates;
import com.markit.pdf.draw.DrawMethodPositionCoordinates;

import java.util.List;

/**
 * @author Oleg Cheban
 * @since 1.0
 */
public class WatermarkPositioner {

    public List<WatermarkPositionCoordinates.Coordinates> defineXY(
            WatermarkAttributes attr, int imageWidth, int imageHeight, int watermarkWidth, int watermarkHeight) {
        return new DrawMethodPositionCoordinates(imageWidth, imageHeight, watermarkWidth, watermarkHeight)
                .getCoordinatesForAttributes(attr);
    }
}
