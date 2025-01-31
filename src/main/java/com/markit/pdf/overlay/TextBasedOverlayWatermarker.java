package com.markit.pdf.overlay;

import com.markit.api.WatermarkAttributes;
import com.markit.api.positioning.WatermarkPositionCoordinates;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.util.Matrix;

import java.io.IOException;
import java.util.Optional;

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
            float x = c.getX() + textWidth / 2;
            float y = c.getY() + textHeight / 2;
            contentStream.setTextMatrix(setRotationMatrix(x, y, textWidth, textHeight, attr.getRotationDegrees()));
            contentStream.showText(attr.getText());
            contentStream.endText();

            if (attr.getTrademark()) {
                trademarkHandler.overlayTrademark(contentStream, attr, textWidth, textHeight, x, y, font, fontSize);
            }
        }
    }

    private Matrix setRotationMatrix(float x, float y, float textWidth, float textHeight, int rotationDegrees){
        var matrix = new Matrix();
        matrix.translate(x, y);
        rotate(matrix, rotationDegrees);
        matrix.translate(-textWidth / 2, -textHeight / 2);
        return matrix;
    }

    private void rotate(Matrix matrix, int rotationDegrees){
        if (rotationDegrees != 0){
            matrix.rotate(Math.toRadians(rotationDegrees));
        }
    }
}
