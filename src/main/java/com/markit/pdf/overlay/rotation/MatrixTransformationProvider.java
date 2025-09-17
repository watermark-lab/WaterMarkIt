package com.markit.pdf.overlay.rotation;

import com.markit.api.positioning.WatermarkPositionCoordinates;
import com.markit.servicelocator.Prioritizable;
import org.apache.pdfbox.util.Matrix;

public interface MatrixTransformationProvider extends Prioritizable {
    Matrix createRotationMatrix(
            WatermarkPositionCoordinates.Coordinates coordinates,
            float watermarkWidth,
            float watermarkHeight,
            int rotationDegrees,
            TransformationType type
    );
}
