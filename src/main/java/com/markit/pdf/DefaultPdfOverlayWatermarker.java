package com.markit.pdf;

import com.markit.api.WatermarkPositionCoordinates;
import com.markit.api.WatermarkPosition;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.state.PDExtendedGraphicsState;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Oleg Cheban
 * @since 1.0
 */
@SuppressWarnings("deprecation")
public class DefaultPdfOverlayWatermarker implements OverlayPdfWatermarker {
    private final int TEXT_SIZE = 20;
    @Override
    public void watermark(
            PDDocument document,
            int pageIndex,
            String text,
            int textSize,
            Color color,
            WatermarkPosition position,
            boolean trademark) throws IOException {
        PDPage page = document.getPage(pageIndex);
        PDRectangle mediaBox = page.getMediaBox();
        float pageWidth = mediaBox.getWidth();
        float pageHeight = mediaBox.getHeight();
        try (PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true, true)) {
            PDExtendedGraphicsState transparencyState = new PDExtendedGraphicsState();
            transparencyState.setNonStrokingAlphaConstant(0.5f);
            contentStream.setGraphicsStateParameters(transparencyState);
            overlayWatermark(contentStream, text, textSize, color, pageWidth, pageHeight, loadAreal(document), position, trademark);
        }
    }

    private PDType0Font loadAreal(PDDocument document) throws IOException {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        final String path = "font/arialbd.ttf";
        InputStream arialFont = classloader.getResourceAsStream(path);
        return PDType0Font.load(document, arialFont);
    }

    private void overlayWatermark(
            PDPageContentStream contentStream,
            String text,
            int textSize,
            Color color,
            float pageWidth,
            float pageHeight,
            PDType0Font font,
            WatermarkPosition position,
            boolean trademark) throws IOException {
        final int fontSize = textSize == 0 ? TEXT_SIZE : textSize;
        contentStream.beginText();
        contentStream.setFont(font, fontSize);
        contentStream.setNonStrokingColor(color);
        float textWidth = font.getStringWidth(text) / 1000 * fontSize;
        float textHeight = font.getFontDescriptor().getCapHeight() / 1000 * fontSize;
        var coordinates = defineXY(position, (int) pageWidth, (int) pageHeight, (int) textWidth, (int) textHeight);

        contentStream.setTextTranslation(coordinates.getX(), coordinates.getY());
        contentStream.showText(text);
        contentStream.endText();

        if (trademark){
            overlayTrademark(contentStream, textSize, textWidth, textHeight, coordinates.getX(), coordinates.getY(), font, color);
        }
    }

    private WatermarkPositionCoordinates.Coordinates defineXY(WatermarkPosition position, int iw, int ih, int ww, int wh){
        var c = new OverlayMethodPositionCoordinates(iw, ih, ww, wh);
        switch (position){
            case CENTER: return c.center();
            case TOP_LEFT: return c.topLeft();
            case TOP_RIGHT: return c.topRight();
            case BOTTOM_LEFT: return c.bottomLeft();
            case BOTTOM_RIGHT: return c.bottomRight();
            default:
                throw new RuntimeException("undefined position");
        }
    }

    private void overlayTrademark(PDPageContentStream contentStream, int textSize, float textWidth, float textHeight, float xCenter, float yCenter, PDType0Font font, Color color) throws IOException {
        final String registeredTrademark = "®";
        final int registeredTrademarkFontSize = (textSize == 0 ? TEXT_SIZE : textSize) / 2;
        float symbolOffsetX = xCenter + textWidth;
        float symbolOffsetY = yCenter + textHeight / 2 + 5;

        contentStream.beginText();
        contentStream.setFont(font, registeredTrademarkFontSize);
        contentStream.setNonStrokingColor(color);
        contentStream.setTextTranslation(symbolOffsetX, symbolOffsetY);
        contentStream.showText(registeredTrademark);
        contentStream.endText();
    }
}
