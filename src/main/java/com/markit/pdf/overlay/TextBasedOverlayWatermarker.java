package com.markit.pdf.overlay;

import com.markit.api.WatermarkAttributes;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
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

    public void overlay(PDPageContentStream contentStream, PDRectangle pdRectangle, PDType0Font font, WatermarkAttributes attr) throws IOException {
        final int fontSize = attr.getSize() == 0 ? TEXT_SIZE : attr.getSize();
        contentStream.beginText();
        contentStream.setFont(PDType1Font.TIMES_BOLD, fontSize);
        contentStream.setNonStrokingColor(attr.getColor());
        float textWidth = font.getStringWidth(attr.getText()) / 1000 * fontSize;
        float textHeight = font.getFontDescriptor().getCapHeight() / 1000 * fontSize;
        var coordinates = positioner.defineXY(attr, (int) pdRectangle.getWidth(), (int) pdRectangle.getHeight (), (int) textWidth, (int) textHeight);
        float x = coordinates.get(0).getX() + textWidth / 2;
        float y = coordinates.get(0).getY() + textHeight / 2;
        contentStream.setTextMatrix(setRotationMatrix(x, y, textWidth, textHeight, attr.getRotationDegrees()));
        contentStream.showText(attr.getText());
        contentStream.endText();

        if (attr.getTrademark()) {
            trademarkHandler.overlayTrademark(contentStream, attr, textWidth, textHeight, x, y, font, fontSize);
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
