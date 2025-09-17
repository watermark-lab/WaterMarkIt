package com.markit.pdf.overlay;

import com.markit.api.WatermarkAttributes;
import com.markit.api.positioning.WatermarkPositionCoordinates;
import com.markit.pdf.overlay.positioning.WatermarkPositioner;
import com.markit.pdf.overlay.rotation.MatrixTransformationProvider;
import com.markit.pdf.overlay.rotation.TransformationType;
import com.markit.servicelocator.ServiceFactory;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.IOException;

public class DefaultImageBasedOverlayWatermarker implements ImageBasedOverlayWatermarker {

    @Override
    public void overlay(PDPageContentStream contentStream, PDImageXObject imageXObject, PDRectangle pdRectangle, WatermarkAttributes attr) throws IOException {
        float imageWidth = (int) (imageXObject.getWidth() * (attr.getSize() / 300f));
        float imageHeight = (int) (imageXObject.getHeight() * (attr.getSize() / 300f));

        var coordinates = WatermarkPositioner.defineXY(
                attr, (int) pdRectangle.getWidth(), (int) pdRectangle.getHeight(), (int) imageWidth, (int) imageHeight);

        for (WatermarkPositionCoordinates.Coordinates c : coordinates) {
            if (attr.getRotationDegrees() != 0) {
                applyRotationAndDraw(contentStream, imageXObject, c, imageWidth, imageHeight, attr.getRotationDegrees());
            } else {
                contentStream.drawImage(imageXObject, c.getX(), c.getY(), imageWidth, imageHeight);
            }
        }
    }

    private void applyRotationAndDraw(PDPageContentStream contentStream, PDImageXObject imageXObject,
                                      WatermarkPositionCoordinates.Coordinates c, float width, float height,
                                      int rotationDegrees) throws IOException {
        contentStream.saveGraphicsState();

        var textTransformationProvider = (MatrixTransformationProvider) ServiceFactory.getInstance()
                .getService(MatrixTransformationProvider.class);

        var rotationMatrix = textTransformationProvider.createRotationMatrix(
                c, width, height, rotationDegrees, TransformationType.IMAGE_TRANSFORM);

        contentStream.transform(rotationMatrix);
        contentStream.drawImage(imageXObject, c.getX(), c.getY(), width, height);
        contentStream.restoreGraphicsState();
    }

    @Override
    public int getPriority() {
        return DEFAULT_PRIORITY;
    }
}
