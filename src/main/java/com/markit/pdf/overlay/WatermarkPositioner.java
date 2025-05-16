package com.markit.pdf.overlay;

import com.markit.core.WatermarkAttributes;
import com.markit.core.positioning.WatermarkPositionCoordinates;

import java.util.List;

/**
 * @author Oleg Cheban
 * @since 1.0
 */
public class WatermarkPositioner {

    public List<WatermarkPositionCoordinates.Coordinates> defineXY(
            WatermarkAttributes attr, int imageWidth, int imageHeight, int watermarkWidth, int watermarkHeight) {
        return new OverlayMethodPositionCoordinates(imageWidth, imageHeight, watermarkWidth, watermarkHeight)
                .getCoordinatesForAttributes(attr);
    }
}
