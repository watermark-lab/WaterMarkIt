package com.markit.pdf.overlay;

import com.markit.api.WatermarkAttributes;
import com.markit.api.positioning.WatermarkPositionCoordinates;
import com.markit.servicelocator.ServiceFactory;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.util.Matrix;

import java.io.IOException;

public class DefaultTextBasedOverlayWatermarker implements TextBasedOverlayWatermarker {

    @Override
    public int getPriority() {
        return DEFAULT_PRIORITY;
    }

    @Override
    public void overlay(PDPageContentStream contentStream, PDRectangle pdRectangle, WatermarkAttributes attr) throws IOException {
        var coordinates = WatermarkPositioner.defineXY(
                attr, (int) pdRectangle.getWidth(), (int) pdRectangle.getHeight (),
                (int) attr.getPdfWatermarkTextWidth(), (int) attr.getPdfWatermarkTextHeight());

        for (WatermarkPositionCoordinates.Coordinates c : coordinates) {
            contentStream.beginText();
            contentStream.setFont(attr.getPdfFont(), attr.getPdfTextSize());
            contentStream.setNonStrokingColor(attr.getColor());
            contentStream.setTextMatrix(setRotationMatrix(c, attr.getPdfWatermarkTextWidth(), attr.getPdfWatermarkTextHeight(), attr.getRotationDegrees()));
            contentStream.showText(attr.getText());
            contentStream.endText();

            if (attr.getTrademark()) {
                var trademarkService = (TrademarkService) ServiceFactory.getInstance().getService(TrademarkService.class);
                trademarkService.overlayTrademark(contentStream, attr, c);
            }
        }
    }

    private Matrix setRotationMatrix(WatermarkPositionCoordinates.Coordinates c, float textWidth, float textHeight, int rotationDegrees){
        float translateX = c.getX() + textWidth / 2;
        float translateY = c.getY() + textHeight / 2;
        var matrix = new Matrix();
        matrix.translate(translateX, translateY);
        rotate(matrix, rotationDegrees);
        matrix.translate(-textWidth/2, -textHeight/2);
        return matrix;
    }

    private void rotate(Matrix matrix, int rotationDegrees){
        if (rotationDegrees != 0){
            matrix.rotate(Math.toRadians(rotationDegrees));
        }
    }
}
