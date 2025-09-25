package com.markit.pdf.overlay.rotation;

import com.markit.api.positioning.Coordinates;
import com.markit.servicelocator.Prioritizable;
import org.apache.pdfbox.util.Matrix;

public interface MatrixTransformationProvider extends Prioritizable {

    Matrix createRotationMatrix(Coordinates coordinates, float watermarkWidth, float watermarkHeight, int rotationDegrees, TransformationType type);

}
