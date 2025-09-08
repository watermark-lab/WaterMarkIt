package com.markit.pdf.overlay;

import com.markit.api.WatermarkAttributes;
import com.markit.api.positioning.WatermarkPositionCoordinates;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.util.Matrix;

import java.io.IOException;

public class DefaultImageBasedOverlayWatermarker implements ImageBasedOverlayWatermarker {

    @Override
    public int getPriority() {
        return DEFAULT_PRIORITY;
    }

    @Override
    public void overlay(PDPageContentStream contentStream, PDImageXObject imageXObject, PDRectangle pdRectangle, WatermarkAttributes attr) throws IOException {
        float imageWidth = (int) (imageXObject.getWidth() * (attr.getSize() / 300f));
        float imageHeight = (int) (imageXObject.getHeight() * (attr.getSize() / 300f));

        var coordinates = WatermarkPositioner.defineXY(
                attr, (int) pdRectangle.getWidth(), (int) pdRectangle.getHeight(), (int) imageWidth, (int) imageHeight);

        for (WatermarkPositionCoordinates.Coordinates c : coordinates) {

            if (attr.getRotationDegrees() != 0){
                Matrix rotationMatrix = setTransformationMatrix(c, imageWidth, imageHeight, attr.getRotationDegrees());
                contentStream.saveGraphicsState();
                contentStream.transform(rotationMatrix);
            }
            contentStream.drawImage(imageXObject, c.getX(), c.getY(), imageWidth, imageHeight);
            if (attr.getRotationDegrees() != 0) {
                contentStream.restoreGraphicsState();
            }
        }
    }

    private Matrix setTransformationMatrix(WatermarkPositionCoordinates.Coordinates c, float width, float height, int rotationDegrees) {
        var matrix = new Matrix();
        float translateX = c.getX() + (width / 2);
        float translateY = c.getY() + (height / 2);
        matrix.translate(translateX, translateY);
        rotate(matrix, rotationDegrees);
        matrix.translate(-translateX, -translateY);
        return matrix;
    }

    private void rotate(Matrix matrix, int rotationDegrees){
        if (rotationDegrees != 0){
            matrix.rotate(Math.toRadians(rotationDegrees));
        }
    }
}
