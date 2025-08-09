package com.markit.pdf.overlay;

import com.markit.api.WatermarkAttributes;
import com.markit.api.positioning.WatermarkPositionCoordinates;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.util.Matrix;

import java.io.IOException;

/**
 * @author Oleg Cheban
 * @since 1.0
 */
@SuppressWarnings("deprecation")
public class TrademarkHandler {
    private static final String TRADEMARK_SYMBOL = "®";

    public void overlayTrademark(
            PDPageContentStream contentStream,
            WatermarkAttributes attr,
            float textWidth,
            float textHeight,
            WatermarkPositionCoordinates.Coordinates c,
            PDType1Font font,
            int fontSize) throws IOException {

        final int trademarkFontSize = fontSize / 2;
        contentStream.beginText();
        contentStream.setFont(font, trademarkFontSize);
        contentStream.setNonStrokingColor(attr.getColor());
        contentStream.setTextMatrix(setTransformationMatrix(c, textWidth, textHeight, attr.getRotationDegrees()));
        contentStream.showText(TRADEMARK_SYMBOL);
        contentStream.endText();
    }

    private Matrix setTransformationMatrix(WatermarkPositionCoordinates.Coordinates c, float textWidth, float textHeight, int rotationDegrees) {
        Matrix matrix = new Matrix();
        matrix.translate(c.getX() + textWidth / 2, c.getY() + textHeight / 2);
        rotate(matrix, rotationDegrees);
        matrix.translate(textWidth / 2, textHeight / 2);
        return matrix;
    }

    private void rotate(Matrix matrix, int rotationDegrees){
        if (rotationDegrees != 0){
            matrix.rotate(Math.toRadians(rotationDegrees));
        }
    }
}
