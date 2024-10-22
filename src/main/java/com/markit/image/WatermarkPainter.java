package com.markit.image;

import com.markit.api.WatermarkAttributes;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * @author Oleg Cheban
 * @since 1.0
 */
public class WatermarkPainter {
    public void draw(Graphics2D g2d, BufferedImage image, int baseFontSize, WatermarkAttributes attr, WatermarkPositioner positioner) {
        var alphaChannel = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f);
        var baseFont = new Font("Arial", Font.BOLD, baseFontSize);
        configureGraphics(g2d, alphaChannel, attr.getColor(), baseFont);

        FontRenderContext frc = g2d.getFontRenderContext();
        TextLayout watermarkLayout = new TextLayout(attr.getText(), baseFont, frc);
        Rectangle2D rect = watermarkLayout.getBounds();

        var coordinates = positioner.defineXY(attr.getPosition(), image.getWidth(), image.getHeight(), (int) rect.getWidth(), (int) rect.getHeight());
        coordinates.forEach(v -> drawWatermark(g2d, watermarkLayout, attr, rect, v.getX(), v.getY(), baseFont, baseFontSize));
    }

    private void configureGraphics(Graphics2D g2d, AlphaComposite alphaChannel, Color color, Font font) {
        g2d.setComposite(alphaChannel);
        g2d.setColor(color);
        g2d.setFont(font);
    }

    private void drawWatermark(Graphics2D g2d, TextLayout watermarkLayout, WatermarkAttributes attr, Rectangle2D rect, int x, int y, Font baseFont, int baseFontSize) {
        if (attr.getRotation() != 0) {
            applyRotation(g2d, attr.getRotation(), x, y, rect);
        }
        watermarkLayout.draw(g2d, x, y);

        if (attr.getTrademark()) {
            drawTrademark(g2d, baseFont, baseFontSize, rect, x, y);
        }
    }

    private void applyRotation(Graphics2D g2d, int rotation, double x, double y, Rectangle2D rect) {
        double centerX = x + rect.getWidth() / 2;
        double centerY = y + rect.getHeight() / 2;
        g2d.rotate(-Math.toRadians(rotation), centerX, centerY);
    }

    private void drawTrademark(Graphics2D g2d, Font baseFont, int baseFontSize, Rectangle2D rect, int centerX, int centerY) {
        FontRenderContext frc = g2d.getFontRenderContext();
        Font smallFont = baseFont.deriveFont((float) baseFontSize / 2);
        TextLayout trademarkLayout = new TextLayout("®", smallFont, frc);
        trademarkLayout.draw(g2d, (float) (centerX + rect.getWidth()) + 5, centerY - (baseFontSize / 1.5f));
    }
}
