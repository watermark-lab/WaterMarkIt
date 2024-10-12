package com.markit.image;

import com.markit.api.WatermarkAttributes;
import com.markit.api.WatermarkPositionCoordinates;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * @author Oleg Cheban
 * @since 1.0
 */
public class WatermarkRenderer {
    public void drawWatermark(Graphics2D g2d, BufferedImage image, int baseFontSize, WatermarkAttributes attr, WatermarkPositioner positioner) {
        var alphaChannel = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f);
        var baseFont = new Font("Arial", Font.BOLD, baseFontSize);
        g2d.setComposite(alphaChannel);
        g2d.setColor(attr.getColor());
        g2d.setFont(baseFont);
        FontRenderContext frc = g2d.getFontRenderContext();
        TextLayout layout = new TextLayout(attr.getText(), baseFont, frc);
        Rectangle2D rect = layout.getBounds();
        var coordinates = positioner.defineXY(attr.getPosition(), image.getWidth(), image.getHeight(), (int) rect.getWidth(), (int) rect.getHeight());
        if (attr.getRotation() != 0){
            applyRotation(g2d, attr, coordinates, rect);
        }
        layout.draw(g2d, coordinates.getX(), coordinates.getY());
        if (attr.getTrademark()) {
            drawTrademark(g2d, baseFont, baseFontSize, rect, coordinates.getX(), coordinates.getY());
        }
    }

    private void applyRotation(Graphics2D g2d, WatermarkAttributes attr, WatermarkPositionCoordinates.Coordinates coordinates, Rectangle2D rect) {
        double centerX = coordinates.getX() + rect.getWidth() / 2;
        double centerY = coordinates.getY() + rect.getHeight() / 2;
        double rotationAngle = Math.toRadians(attr.getRotation()); //Converting degrees to radians
        g2d.rotate(rotationAngle, centerX, centerY);
    }

    private void drawTrademark(Graphics2D g2d, Font baseFont, int baseFontSize, Rectangle2D rect, int centerX, int centerY) {
        FontRenderContext frc = g2d.getFontRenderContext();
        Font smallFont = baseFont.deriveFont((float) baseFontSize / 2);
        TextLayout trademarkLayout = new TextLayout("®", smallFont, frc);
        trademarkLayout.draw(g2d, (float) (centerX + rect.getWidth()) + 5, centerY - (baseFontSize / 1.5f));
    }

    public int calculateFontSize(int textSize, int imageWidth, int imageHeight) {
        if (textSize > 0) return textSize;
        return Math.min(imageWidth, imageHeight) / 10;
    }
}
