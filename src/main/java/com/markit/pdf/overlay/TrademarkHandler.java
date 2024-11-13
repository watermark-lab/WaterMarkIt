package com.markit.pdf.overlay;

import com.markit.api.TextWatermarkAttributes;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.util.Matrix;

import java.io.IOException;

/**
 * @author Oleg Cheban
 * @since 1.0
 */
@SuppressWarnings("deprecation")
public class TrademarkHandler {
    private static final String TRADEMARK_SYMBOL = "®";

    public void overlayTrademark(PDPageContentStream contentStream, TextWatermarkAttributes attr, float textWidth, float textHeight, float xCenter, float yCenter, PDType0Font font, int fontSize) throws IOException {
        final int trademarkFontSize = fontSize / 2;
        contentStream.beginText();
        contentStream.setFont(font, trademarkFontSize);
        contentStream.setNonStrokingColor(attr.getColor());
        Matrix trademarkTransform = new Matrix();
        trademarkTransform.translate(xCenter, yCenter);
        trademarkTransform.rotate(Math.toRadians(attr.getRotation()));
        trademarkTransform.translate(textWidth / 2, textHeight / 2); // Move to top-right corner
        contentStream.setTextMatrix(trademarkTransform);
        contentStream.showText(TRADEMARK_SYMBOL);
        contentStream.endText();
    }
}
