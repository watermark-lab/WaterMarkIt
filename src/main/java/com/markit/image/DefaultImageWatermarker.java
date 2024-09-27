package com.markit.image;

import com.markit.api.FileType;
import com.markit.pdf.DrawMethodPositionCoordinates;
import com.markit.api.WatermarkPositionCoordinates;
import com.markit.api.WatermarkPosition;

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
    public byte[] watermark(
            byte[] sourceImageBytes,
            FileType fileType,
            String text,
            int textSize,
            Color color,
            boolean trademark,
            WatermarkPosition position) throws IOException {
        if (isByteArrayEmpty(sourceImageBytes)){
            return sourceImageBytes;
        }
        var sourceImage = convertToBufferedImage(sourceImageBytes);
        var g2d = sourceImage.createGraphics();
        int imageWidth = sourceImage.getWidth();
        int imageHeight = sourceImage.getHeight();
        int baseFontSize = calculateFontSize(textSize, imageWidth, imageHeight);
        drawWatermark(g2d, sourceImage, baseFontSize, text, color, trademark, position);
        g2d.dispose();
        return convertToByteArray(sourceImage, fileType);
    }

    private Boolean isByteArrayEmpty(byte[] byteArray) {
        return byteArray == null || byteArray.length == 0;
    }

    private BufferedImage convertToBufferedImage(byte[] imageBytes) throws IOException {
        return ImageIO.read(new ByteArrayInputStream(imageBytes));
    }

    private int calculateFontSize(int textSize, int imageWidth, int imageHeight) {
        if (textSize > 0) return textSize;
        return Math.min(imageWidth, imageHeight) / 10;
    }

    private void drawWatermark(Graphics2D g2d, BufferedImage image, int baseFontSize, String watermarkText, Color watermarkColor, boolean trademark, WatermarkPosition position) {
        var alphaChannel = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f);
        var baseFont = new Font("Arial", Font.BOLD, baseFontSize);
        g2d.setComposite(alphaChannel);
        g2d.setColor(watermarkColor);
        g2d.setFont(baseFont);
        FontRenderContext frc = g2d.getFontRenderContext();
        TextLayout layout = new TextLayout(watermarkText, baseFont, frc);
        Rectangle2D rect = layout.getBounds();
        var coordinates = defineXY(position, image.getWidth(), image.getHeight(), (int) rect.getWidth(), (int) rect.getHeight());
        layout.draw(g2d, coordinates.getX(), coordinates.getY());
        if (trademark){
            drawTrademark(g2d, baseFont, baseFontSize, rect, coordinates.getX(), coordinates.getY());
        }
    }

    private WatermarkPositionCoordinates.Coordinates defineXY(WatermarkPosition position, int iw, int ih, int ww, int wh){
        var c = new DrawMethodPositionCoordinates(iw, ih, ww, wh);
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
