package com.markit.pdf.overlay;

import com.markit.core.WatermarkAttributes;
import com.markit.core.positioning.WatermarkPositionCoordinates;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.util.Matrix;

import java.io.IOException;

public class TextBasedOverlayWatermarker {
    private static final int TEXT_SIZE = 20;
    private final TrademarkHandler trademarkHandler;
    private final WatermarkPositioner positioner;

    public TextBasedOverlayWatermarker(TrademarkHandler trademarkHandler, WatermarkPositioner positioner) {
        this.trademarkHandler = trademarkHandler;
        this.positioner = positioner;
    }

    public void overlay(PDPageContentStream contentStream, PDRectangle pdRectangle, WatermarkAttributes attr) throws IOException {
        final var font =  PDType1Font.TIMES_BOLD;
        final int fontSize = attr.getSize() == 0 ? TEXT_SIZE : attr.getSize();
        float textWidth = font.getStringWidth(attr.getText()) / 1000 * fontSize;
        float textHeight = font.getFontDescriptor().getCapHeight() / 1000 * fontSize;
        var coordinates = positioner.defineXY(attr, (int) pdRectangle.getWidth(), (int) pdRectangle.getHeight (), (int) textWidth, (int) textHeight);

        for (WatermarkPositionCoordinates.Coordinates c : coordinates) {
            contentStream.beginText();
            contentStream.setFont(font, fontSize);
            contentStream.setNonStrokingColor(attr.getColor());
            contentStream.setTextMatrix(setRotationMatrix(c, textWidth, textHeight, attr.getRotationDegrees()));
            contentStream.showText(attr.getText());
            contentStream.endText();

            if (attr.getTrademark()) {
                trademarkHandler.overlayTrademark(contentStream, attr, textWidth, textHeight, c, font, fontSize);
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
