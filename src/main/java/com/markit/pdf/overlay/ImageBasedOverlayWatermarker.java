package com.markit.pdf.overlay;

import com.markit.api.WatermarkAttributes;
import com.markit.api.WatermarkPositionCoordinates;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.util.Matrix;

import java.io.IOException;

public class ImageBasedOverlayWatermarker {
    private final WatermarkPositioner positioner;
    public ImageBasedOverlayWatermarker(WatermarkPositioner positioner) {
        this.positioner = positioner;
    }

    public void overlay(PDPageContentStream contentStream, PDImageXObject imageXObject, PDRectangle pdRectangle, WatermarkAttributes attr) throws IOException {
        float imageWidth = (int) (imageXObject.getWidth() * (attr.getSize() / 100.0));
        float imageHeight = (int) (imageXObject.getHeight() * (attr.getSize() / 100.0));
        var coordinates = positioner.defineXY(attr, (int) pdRectangle.getWidth(), (int) pdRectangle.getHeight(), (int) imageWidth, (int) imageHeight);
        for (WatermarkPositionCoordinates.Coordinates c : coordinates) {
            float x = c.getX();
            float y = c.getY();
            if (attr.getRotationDegrees() != 0){
                Matrix rotationMatrix = setTransformationMatrix(x, y, imageWidth, imageHeight, attr.getRotationDegrees());
                contentStream.saveGraphicsState();
                contentStream.transform(rotationMatrix);
            }
            contentStream.drawImage(imageXObject, x, y, imageWidth, imageHeight);
            if (attr.getRotationDegrees() != 0) {
                contentStream.restoreGraphicsState();
            }
        }
    }

    private Matrix setTransformationMatrix(float x, float y, float width, float height, int rotationDegrees) {
        var matrix = new Matrix();
        float translateX = x + (width / 2);
        float translateY = y + (height / 2);
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
