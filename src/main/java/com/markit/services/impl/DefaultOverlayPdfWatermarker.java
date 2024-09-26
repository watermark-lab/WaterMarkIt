package com.markit.services.impl;

import com.markit.services.OverlayPdfWatermarker;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.state.PDExtendedGraphicsState;
import org.apache.pdfbox.util.Matrix;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Oleg Cheban
 * @since 1.0
 */
@SuppressWarnings("deprecation")
public class DefaultOverlayPdfWatermarker implements OverlayPdfWatermarker {
    @Override
    public void watermark(PDDocument document, int pageIndex, String text, Color color, boolean trademark) throws IOException {
        PDPage page = document.getPage(pageIndex);
        PDRectangle mediaBox = page.getMediaBox();
        float pageWidth = mediaBox.getWidth();
        float pageHeight = mediaBox.getHeight();
        int rotation = page.getRotation();

        try (PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true, true)) {
            PDExtendedGraphicsState transparencyState = new PDExtendedGraphicsState();
            transparencyState.setNonStrokingAlphaConstant(0.5f);
            contentStream.setGraphicsStateParameters(transparencyState);
            if (rotation != 0) {
                contentStream.transform(getRotationMatrix(rotation, pageWidth, pageHeight));
            }
            overlayWatermark(contentStream, text, color, pageWidth, pageHeight, loadAreal(document), rotation, trademark);
            if (rotation != 0) {
                contentStream.restoreGraphicsState();
            }
        }
    }

    private PDType0Font loadAreal(PDDocument document) throws IOException {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        final String path = "font/arialbd.ttf";
        InputStream arialFont = classloader.getResourceAsStream(path);
        return PDType0Font.load(document, arialFont);
    }

    private void overlayWatermark(PDPageContentStream contentStream, String text, Color color, float pageWidth, float pageHeight, PDType0Font font, int rotation, boolean trademark) throws IOException {
        final int fontSize = 50;
        contentStream.beginText();
        contentStream.setFont(font, fontSize);
        contentStream.setNonStrokingColor(color);
        float textWidth = font.getStringWidth(text) / 1000 * fontSize;
        float textHeight = font.getFontDescriptor().getCapHeight() / 1000 * fontSize;
        float xCenter = (pageWidth - textWidth) / 2;
        float yCenter = (pageHeight - textHeight) / 2;

        if (rotation == 90 || rotation == 270) {
            xCenter = (pageHeight - textWidth) / 2;
            yCenter = (pageWidth - textHeight);
        }

        contentStream.setTextTranslation(xCenter, yCenter);
        contentStream.showText(text);
        contentStream.endText();

        if (trademark){
            overlayTrademark(contentStream, textWidth, textHeight, xCenter, yCenter, font, color);
        }
    }

    private void overlayTrademark(PDPageContentStream contentStream, float textWidth, float textHeight, float xCenter, float yCenter, PDType0Font font, Color color) throws IOException {
        final String registeredTrademark = "®";
        final int registeredTrademarkFontSize = 20;

        float symbolOffsetX = xCenter + textWidth;
        float symbolOffsetY = yCenter + textHeight / 2 + 15;
        contentStream.beginText();
        contentStream.setFont(font, registeredTrademarkFontSize);
        contentStream.setNonStrokingColor(color);
        contentStream.setTextTranslation(symbolOffsetX, symbolOffsetY);
        contentStream.showText(registeredTrademark);
        contentStream.endText();
    }

    private Matrix getRotationMatrix(int rotation, float pageWidth, float pageHeight) {
        Matrix matrix = new Matrix();
        switch (rotation) {
            case 90:
                matrix.translate(pageHeight, 0);
                matrix.rotate(Math.toRadians(90));
                break;
            case 180:
                matrix.translate(pageWidth, pageHeight);
                matrix.rotate(Math.toRadians(180));
                break;
            case 270:
                matrix.translate(0, pageWidth);
                matrix.rotate(Math.toRadians(270));
                break;
        }
        return matrix;
    }
}
