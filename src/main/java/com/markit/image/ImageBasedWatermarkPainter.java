package com.markit.image;

import com.markit.api.WatermarkAttributes;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.*;

/**
 * @author Oleg Cheban
 * @since 1.2.0
 */
public class ImageBasedWatermarkPainter {
    public void draw(Graphics2D g2d, BufferedImage image, WatermarkAttributes attr) {
        BufferedImage watermarkImage = attr.getImage().get();
        var alphaChannel = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, attr.getOpacity());
        configureGraphics(g2d, alphaChannel);
        int watermarkWidth = (int) (watermarkImage.getWidth() * (attr.getSize() / 100.0));
        int watermarkHeight = (int) (watermarkImage.getHeight() * (attr.getSize() / 100.0));
        int x = (image.getWidth() - watermarkWidth) / 2;
        int y = (image.getHeight() - watermarkHeight) / 2;

        applyRotation(g2d, attr.getRotation(), x, y, watermarkWidth, watermarkHeight);

        drawWatermark(g2d, watermarkImage, x, y, watermarkWidth, watermarkHeight);
    }

    private void configureGraphics(Graphics2D g2d, AlphaComposite alphaChannel) {
        g2d.setComposite(alphaChannel);
    }

    private void drawWatermark(Graphics2D g2d, BufferedImage watermarkImage, int x, int y, int width, int height) {
        g2d.drawImage(watermarkImage, x, y, width, height, null);
    }

    private void applyRotation(Graphics2D g2d, int rotation, int x, int y, int width, int height) {
        double centerX = x + width / 2.0;
        double centerY = y + height / 2.0;
        g2d.rotate(-Math.toRadians(rotation), centerX, centerY);
    }

}