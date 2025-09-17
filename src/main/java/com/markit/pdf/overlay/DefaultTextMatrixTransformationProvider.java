package com.markit.pdf.overlay;

import com.markit.api.positioning.WatermarkPositionCoordinates;
import org.apache.pdfbox.util.Matrix;

public class DefaultTextMatrixTransformationProvider implements TextMatrixTransformationProvider {

    @Override
    public Matrix createTextRotationMatrix(WatermarkPositionCoordinates.Coordinates coordinates, float textWidth, float textHeight, int rotationDegrees) {
        float translateX = coordinates.getX() + textWidth / 2;
        float translateY = coordinates.getY() + textHeight / 2;
        var matrix = new Matrix();
        matrix.translate(translateX, translateY);

        if (rotationDegrees != 0) {
            matrix.rotate(Math.toRadians(rotationDegrees));
        }

        matrix.translate(-textWidth / 2, -textHeight / 2);
        return matrix;
    }

    @Override
    public int getPriority() {
        return DEFAULT_PRIORITY;
    }
}
