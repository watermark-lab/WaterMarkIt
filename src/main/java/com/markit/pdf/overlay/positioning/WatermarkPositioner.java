package com.markit.pdf.overlay.positioning;

import com.markit.api.WatermarkAttributes;
import com.markit.api.positioning.Coordinates;

import java.util.List;

/**
 * @author Oleg Cheban
 * @since 1.0
 */
public class WatermarkPositioner {

    public static List<Coordinates> defineXY(
            WatermarkAttributes attr, int imageWidth, int imageHeight, int watermarkWidth, int watermarkHeight) {
        return new OverlayMethodPositionCoordinates(imageWidth, imageHeight, watermarkWidth, watermarkHeight)
                .getCoordinatesForAttributes(attr);
    }
}
