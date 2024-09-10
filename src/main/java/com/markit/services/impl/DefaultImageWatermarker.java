package com.markit.services.impl;

import com.markit.services.ImageWatermarker;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author Oleg Cheban
 * @since 1.0
 */
public class DefaultImageWatermarker implements ImageWatermarker {
    @Override
    public byte[] watermark(byte[] sourceImageBytes, FileType fileType, String watermarkText, Color watermarkColor, Boolean trademark) throws IOException {
        if (isByteArrayEmpty(sourceImageBytes)){
            return sourceImageBytes;
        }
        var sourceImage = convertToBufferedImage(sourceImageBytes);
        var g2d = sourceImage.createGraphics();
        int imageWidth = sourceImage.getWidth();
        int imageHeight = sourceImage.getHeight();
        int baseFontSize = calculateFontSize(imageWidth, imageHeight);
        drawWatermark(g2d, sourceImage, baseFontSize, watermarkText, watermarkColor, trademark);
        g2d.dispose();
        return convertToByteArray(sourceImage, fileType);
    }

    private Boolean isByteArrayEmpty(byte[] byteArray) {
        return byteArray == null || byteArray.length == 0;
    }

    private BufferedImage convertToBufferedImage(byte[] imageBytes) throws IOException {
        return ImageIO.read(new ByteArrayInputStream(imageBytes));
    }

    private int calculateFontSize(int imageWidth, int imageHeight) {
        return Math.min(imageWidth, imageHeight) / 10;
    }

    private void drawWatermark(Graphics2D g2d, BufferedImage image, int baseFontSize, String watermarkText, Color watermarkColor, Boolean trademark) {
        var alphaChannel = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f);
        var baseFont = new Font("Arial", Font.BOLD, baseFontSize);
        g2d.setComposite(alphaChannel);
        g2d.setColor(watermarkColor);
        g2d.setFont(baseFont);
        FontRenderContext frc = g2d.getFontRenderContext();
        TextLayout layout = new TextLayout(watermarkText, baseFont, frc);
        Rectangle2D rect = layout.getBounds();
        int centerX = (image.getWidth() - (int) (rect.getWidth())) / 2;
        int centerY = image.getHeight() / 2;
        layout.draw(g2d, centerX, centerY);

        if (trademark){
            drawTrademark(g2d, baseFont, baseFontSize, rect, centerX, centerY);
        }
    }

    private void drawTrademark(Graphics2D g2d, Font baseFont, int baseFontSize, Rectangle2D rect, int centerX, int centerY){
        FontRenderContext frc = g2d.getFontRenderContext();
        Font smallFont = baseFont.deriveFont((float) baseFontSize / 2);
        TextLayout trademarkLayout = new TextLayout("®", smallFont, frc);
        trademarkLayout.draw(g2d, (float) (centerX + rect.getWidth()) + 5, centerY - (baseFontSize / 1.5f));
    }

    private byte[] convertToByteArray(BufferedImage image, FileType fileType) throws IOException {
        var baos = new ByteArrayOutputStream();
        ImageIO.write(image, fileType.name(), baos);
        return baos.toByteArray();
    }
}
