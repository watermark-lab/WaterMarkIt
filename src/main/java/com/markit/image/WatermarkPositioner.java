package com.markit.image;

import com.markit.api.WatermarkAttributes;
import com.markit.api.positioning.Coordinates;
import com.markit.image.positioning.DrawMethodPositionCoordinates;

import java.util.List;

/**
 * @author Oleg Cheban
 * @since 1.0
 */
public class WatermarkPositioner {

    public static List<Coordinates> defineXY(
            WatermarkAttributes attr, int imageWidth, int imageHeight, int watermarkWidth, int watermarkHeight) {
        return new DrawMethodPositionCoordinates(imageWidth, imageHeight, watermarkWidth, watermarkHeight)
                .getCoordinatesForAttributes(attr);
    }
}
