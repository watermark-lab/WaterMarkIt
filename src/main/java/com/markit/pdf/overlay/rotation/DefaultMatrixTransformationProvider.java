package com.markit.pdf.overlay.rotation;

import com.markit.api.positioning.Coordinates;
import org.apache.pdfbox.util.Matrix;

public class DefaultMatrixTransformationProvider implements MatrixTransformationProvider {

    @Override
    public Matrix createRotationMatrix(
            Coordinates coordinates,
            float watermarkWidth,
            float watermarkHeight,
            int rotationDegrees,
            TransformationType type) {
        float translateX = coordinates.getX() + watermarkWidth / 2;
        float translateY = coordinates.getY() + watermarkHeight / 2;
        var matrix = new Matrix();

        matrix.translate(translateX, translateY);

        matrix.rotate(Math.toRadians(rotationDegrees));

        /**
         * Different back-transformation logic is required due to the fundamental difference
         * in how PDFBox handles coordinate systems for images versus text:
         *
         * For IMAGES:
         * - drawImage() coordinates specify the bottom-left corner of the image
         * - The image is drawn from this fixed point outward
         * - To rotate around center, we must return the coordinate system back to its
         *   original position: translate(-translateX, -translateY)
         *
         * For TEXT:
         * - Text transformation matrix defines a new coordinate system for text rendering
         * - After translate(centerX, centerY), the origin (0,0) moves to the text center
         * - Text must then be positioned relative to this new rotated origin
         * - Therefore we translate by half dimensions from the new center: translate(-width/2, -height/2)
         */

        switch (type) {
            case IMAGE_TRANSFORM:
                matrix.translate(-translateX, -translateY);
                break;
            case TEXT_TRANSFORM:
                matrix.translate(-watermarkWidth / 2, -watermarkHeight / 2);
                break;
        }

        return matrix;
    }

    @Override
    public int getPriority() {
        return DEFAULT_PRIORITY;
    }
}
