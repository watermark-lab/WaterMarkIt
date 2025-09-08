package com.markit.image;

import com.markit.api.WatermarkAttributes;
import com.markit.api.positioning.WatermarkPositionCoordinates;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * Default implementation of {@link ImageBasedWatermarkPainter}.
 *
 * @author Oleg Cheban
 * @since 1.3.5
 */
public class DefaultImageBasedWatermarkPainter implements ImageBasedWatermarkPainter {

    @Override
    public int getPriority() {
        return DEFAULT_PRIORITY;
    }

    @Override
    public void draw(Graphics2D g2d, BufferedImage sourceImage, WatermarkAttributes attr) {
        BufferedImage watermarkImage = attr.getImage().get();
        var alphaChannel = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) (attr.getOpacity() / 100.0));
        configureGraphics(g2d, alphaChannel);
        int watermarkWidth = (int) (watermarkImage.getWidth() * (attr.getSize() / 100.0));
        int watermarkHeight = (int) (watermarkImage.getHeight() * (attr.getSize() / 100.0));
        var coordinates = WatermarkPositioner.defineXY(attr, sourceImage.getWidth(), sourceImage.getHeight(), watermarkWidth, watermarkHeight);
        coordinates.forEach(v -> drawWatermark(g2d, watermarkImage, v, watermarkWidth, watermarkHeight, attr.getRotationDegrees()));
    }

    private void configureGraphics(Graphics2D g2d, AlphaComposite alphaChannel) {
        g2d.setComposite(alphaChannel);
    }

    private void drawWatermark(Graphics2D g2d, BufferedImage watermarkImage, WatermarkPositionCoordinates.Coordinates c, int width, int height, int rotation) {
        applyWithOptionalRotation(g2d, rotation, c, width, height, () -> {
            g2d.drawImage(watermarkImage, c.getX(), c.getY(), width, height, null);
        });
    }

    private void applyWithOptionalRotation(Graphics2D g2d, int rotation, WatermarkPositionCoordinates.Coordinates c, int width, int height, Runnable drawAction) {
        var originalTransform = g2d.getTransform();
        if (rotation != 0) {
            applyRotation(g2d, rotation, c, width, height);
        }
        drawAction.run();

        if (rotation != 0) {
            g2d.setTransform(originalTransform);
        }
    }

    private void applyRotation(Graphics2D g2d, int rotation, WatermarkPositionCoordinates.Coordinates c, int width, int height) {
        double centerX = c.getX() + width / 2.0;
        double centerY = c.getY() + height / 2.0;
        g2d.rotate(-Math.toRadians(rotation), centerX, centerY);
    }
}


