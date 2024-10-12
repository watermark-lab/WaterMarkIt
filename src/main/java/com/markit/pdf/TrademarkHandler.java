package com.markit.pdf;

import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

import java.awt.*;
import java.io.IOException;

/**
 * @author Oleg Cheban
 * @since 1.0
 */
@SuppressWarnings("deprecation")
public class TrademarkHandler {
    private static final String TRADEMARK_SYMBOL = "®";
    private static final int SYMBOL_OFFSET = 5;

    public void overlayTrademark(PDPageContentStream contentStream, int textSize, float textWidth, float textHeight, float xCenter, float yCenter, PDType0Font font, Color color) throws IOException {
        final int trademarkFontSize = textSize / 2;
        float symbolOffsetX = xCenter + textWidth;
        float symbolOffsetY = yCenter + textHeight / 2 + SYMBOL_OFFSET;

        contentStream.beginText();
        contentStream.setFont(font, trademarkFontSize);
        contentStream.setNonStrokingColor(color);
        contentStream.setTextTranslation(symbolOffsetX, symbolOffsetY);
        contentStream.showText(TRADEMARK_SYMBOL);
        contentStream.endText();
    }
}
