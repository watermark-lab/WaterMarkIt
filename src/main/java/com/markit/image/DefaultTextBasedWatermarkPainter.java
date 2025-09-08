package com.markit.image;

import com.markit.api.WatermarkAttributes;
import com.markit.api.positioning.WatermarkPositionCoordinates;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * Default implementation of {@link TextBasedWatermarkPainter}.
 *
 * @author Oleg Cheban
 * @since 1.3.5
 */
public class DefaultTextBasedWatermarkPainter implements TextBasedWatermarkPainter {

    @Override
    public int getPriority() {
        return DEFAULT_PRIORITY;
    }

    @Override
    public void draw(Graphics2D g2d, WatermarkAttributes attr) {
        var alphaChannel = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) (attr.getOpacity() / 100.0));
        var fontSize = calculateFontSize(attr.getSize(), attr.getImageWidth(), attr.getImageHeight());
        var fontStyle = attr.isBold() ? Font.BOLD : Font.PLAIN;
        var font = new Font(attr.getFont().getAwtFontName(), fontStyle, fontSize);
        configureGraphics(g2d, alphaChannel, attr.getColor(), font);
        FontRenderContext frc = g2d.getFontRenderContext();
        TextLayout watermarkLayout = new TextLayout(attr.getText(), font, frc);
        Rectangle2D rect = watermarkLayout.getBounds();

        var coordinates = WatermarkPositioner.defineXY(attr, attr.getImageWidth(), attr.getImageHeight(), (int) rect.getWidth(), (int) rect.getHeight());
        coordinates.forEach(v -> drawWatermark(g2d, watermarkLayout, attr, rect, v, font, fontSize));
    }

    private int calculateFontSize(int textSize, int imageWidth, int imageHeight) {
        if (textSize > 0) return textSize;
        return Math.min(imageWidth, imageHeight) / 10;
    }

    private void configureGraphics(Graphics2D g2d, AlphaComposite alphaChannel, Color color, Font font) {
        g2d.setComposite(alphaChannel);
        g2d.setColor(color);
        g2d.setFont(font);
    }

    private void drawWatermark(Graphics2D g2d, TextLayout watermarkLayout, WatermarkAttributes attr, Rectangle2D rect, WatermarkPositionCoordinates.Coordinates c, Font baseFont, int baseFontSize) {
        applyWithOptionalRotation(g2d, attr.getRotationDegrees(), c, rect, () -> {
            watermarkLayout.draw(g2d, c.getX(), c.getY());

            if (attr.getTrademark()) {
                drawTrademark(g2d, baseFont, baseFontSize, rect, c);
            }
        });
    }

    private void applyWithOptionalRotation(Graphics2D g2d, int rotation, WatermarkPositionCoordinates.Coordinates c, Rectangle2D rect, Runnable drawAction) {
        var originalTransform = g2d.getTransform();
        if (rotation != 0) {
            applyRotation(g2d, rotation, c, rect);
        }
        drawAction.run();

        if (rotation != 0) {
            // Restore original transform to avoid accumulating transforms across tiles
            g2d.setTransform(originalTransform);
        }
    }

    private void applyRotation(Graphics2D g2d, int rotation, WatermarkPositionCoordinates.Coordinates c, Rectangle2D rect) {
        double centerX = c.getX() + rect.getWidth() / 2;
        double centerY = c.getY() + rect.getHeight() / 2;
        g2d.rotate(-Math.toRadians(rotation), centerX, centerY);
    }

    private void drawTrademark(Graphics2D g2d, Font baseFont, int baseFontSize, Rectangle2D rect, WatermarkPositionCoordinates.Coordinates c) {
        FontRenderContext frc = g2d.getFontRenderContext();
        Font smallFont = baseFont.deriveFont((float) baseFontSize / 2);
        TextLayout trademarkLayout = new TextLayout("®", smallFont, frc);
        trademarkLayout.draw(g2d, (float) (c.getX() + rect.getWidth()) + 5, c.getY() - (baseFontSize / 2f));
    }
}


