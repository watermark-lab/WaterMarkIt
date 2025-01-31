package com.markit.pdf.overlay;

import com.markit.api.WatermarkAttributes;
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

    public void overlayTrademark(PDPageContentStream contentStream, WatermarkAttributes attr, float textWidth, float textHeight, float x, float y, PDType1Font font, int fontSize) throws IOException {
        final int trademarkFontSize = fontSize / 2;
        contentStream.beginText();
        contentStream.setFont(font, trademarkFontSize);
        contentStream.setNonStrokingColor(attr.getColor());
        contentStream.setTextMatrix(setTransformationMatrix(x, y, textWidth, textHeight, attr.getRotationDegrees()));
        contentStream.showText(TRADEMARK_SYMBOL);
        contentStream.endText();
    }

    private Matrix setTransformationMatrix(float x, float y, float width, float height, int rotationDegrees) {
        Matrix matrix = new Matrix();
        matrix.translate(x, y);
        rotate(matrix, rotationDegrees);
        matrix.translate(width / 2, height / 2);
        return matrix;
    }

    private void rotate(Matrix matrix, int rotationDegrees){
        if (rotationDegrees != 0){
            matrix.rotate(Math.toRadians(rotationDegrees));
        }
    }
}
