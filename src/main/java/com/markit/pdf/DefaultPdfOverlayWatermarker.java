package com.markit.pdf;

import com.markit.api.WatermarkAttributes;
import com.markit.api.WatermarkPositionCoordinates;
import com.markit.api.WatermarkPosition;
import com.markit.exceptions.UnsupportedPositionException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.state.PDExtendedGraphicsState;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author Oleg Cheban
 * @since 1.0
 */
@SuppressWarnings("deprecation")
public class DefaultPdfOverlayWatermarker implements OverlayPdfWatermarker {
    private final int TEXT_SIZE = 20;
    @Override
    public void watermark(PDDocument document, int pageIndex, List<WatermarkAttributes> attrs) throws IOException {
        PDPage page = document.getPage(pageIndex);
        PDRectangle mediaBox = page.getMediaBox();
        float pageWidth = mediaBox.getWidth();
        float pageHeight = mediaBox.getHeight();
        try (PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true, true)) {
            PDExtendedGraphicsState transparencyState = new PDExtendedGraphicsState();
            transparencyState.setNonStrokingAlphaConstant(0.5f);
            contentStream.setGraphicsStateParameters(transparencyState);
            attrs.forEach(attr -> {
                try {
                    overlayWatermark(contentStream, pageWidth, pageHeight, loadAreal(document), attr);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    private PDType0Font loadAreal(PDDocument document) throws IOException {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        final String path = "font/arialbd.ttf";
        InputStream arialFont = classloader.getResourceAsStream(path);
        return PDType0Font.load(document, arialFont);
    }

    private void overlayWatermark(PDPageContentStream contentStream, float pageWidth, float pageHeight, PDType0Font font, WatermarkAttributes attr) throws IOException {
        final int fontSize = attr.getTextSize() == 0 ? TEXT_SIZE : attr.getTextSize();
        contentStream.beginText();
        contentStream.setFont(font, fontSize);
        contentStream.setNonStrokingColor(attr.getColor());
        float textWidth = font.getStringWidth(attr.getText()) / 1000 * fontSize;
        float textHeight = font.getFontDescriptor().getCapHeight() / 1000 * fontSize;
        var coordinates = defineXY(attr.getPosition(), (int) pageWidth, (int) pageHeight, (int) textWidth, (int) textHeight);

        contentStream.setTextTranslation(coordinates.getX(), coordinates.getY());
        contentStream.showText(attr.getText());
        contentStream.endText();

        if (attr.getTrademark()){
            overlayTrademark(contentStream, attr.getTextSize(), textWidth, textHeight, coordinates.getX(), coordinates.getY(), font, attr.getColor());
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
            default: throw new UnsupportedPositionException("Unsupported position: " + position);
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
