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
public class TextBasedWatermarkPainter {
    public void draw(Graphics2D g2d, BufferedImage image, WatermarkAttributes attr, WatermarkPositioner positioner) {
        var alphaChannel = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, attr.getOpacity());
        var fontSize = calculateFontSize(attr.getSize(), image.getWidth(), image.getHeight());
        var font = new Font("Arial", Font.BOLD, fontSize);
        configureGraphics(g2d, alphaChannel, attr.getColor(), font);
        FontRenderContext frc = g2d.getFontRenderContext();
        TextLayout watermarkLayout = new TextLayout(attr.getText(), font, frc);
        Rectangle2D rect = watermarkLayout.getBounds();

        var coordinates = positioner.defineXY(attr, image.getWidth(), image.getHeight(), (int) rect.getWidth(), (int) rect.getHeight());
        coordinates.forEach(v -> drawWatermark(g2d, watermarkLayout, attr, rect, v.getX(), v.getY(), font, fontSize));
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

    private void drawWatermark(Graphics2D g2d, TextLayout watermarkLayout, WatermarkAttributes attr, Rectangle2D rect, int x, int y, Font baseFont, int baseFontSize) {
        applyWithOptionalRotation(g2d, attr.getRotationDegrees(), x, y, rect, () -> {
            watermarkLayout.draw(g2d, x, y);

            if (attr.getTrademark()) {
                drawTrademark(g2d, baseFont, baseFontSize, rect, x, y);
            }
        });
    }

    private void applyWithOptionalRotation(Graphics2D g2d, int rotation, int x, int y, Rectangle2D rect, Runnable drawAction) {
        var originalTransform = g2d.getTransform();
        if (rotation != 0) {
            applyRotation(g2d, rotation, x, y, rect);
        }
        drawAction.run();

        if (rotation != 0) {
            /**
             * Restores the original transformation matrix of the Graphics2D object.
             * This is crucial to ensure that subsequent drawing operations (in case of TILED position) are not affected
             * by the transformations (e.g., rotation) applied to the current watermark.
             * Without this reset, transformations would accumulate, leading to incorrect
             * placement or rendering of other elements.
             */
            g2d.setTransform(originalTransform);
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
