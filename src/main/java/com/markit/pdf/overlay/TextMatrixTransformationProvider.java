package com.markit.pdf.overlay;

import com.markit.api.positioning.WatermarkPositionCoordinates;
import com.markit.servicelocator.Prioritizable;
import org.apache.pdfbox.util.Matrix;

public interface TextMatrixTransformationProvider extends Prioritizable {
    Matrix createTextRotationMatrix(WatermarkPositionCoordinates.Coordinates coordinates, float textWidth, float textHeight, int rotationDegrees);
}
