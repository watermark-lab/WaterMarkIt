package com.markit.pdf.overlay;

import com.markit.api.WatermarkAttributes;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

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
        float x = coordinates.get(0).getX();
        float y = coordinates.get(0).getY();
        contentStream.drawImage(imageXObject, x, y, imageWidth, imageHeight);
    }
}
