package com.markit.image;

import com.markit.api.WatermarkAttributes;

import java.awt.*;
import java.awt.image.*;

/**
 * @author Oleg Cheban
 * @since 1.2.0
 */
public class ImageBasedWatermarkPainter {
    public void draw(Graphics2D g2d, BufferedImage image, WatermarkAttributes attr, WatermarkPositioner positioner) {
        BufferedImage watermarkImage = attr.getImage().get();
        var alphaChannel = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, attr.getOpacity());
        configureGraphics(g2d, alphaChannel);
        int watermarkWidth = (int) (watermarkImage.getWidth() * (attr.getSize() / 100.0));
        int watermarkHeight = (int) (watermarkImage.getHeight() * (attr.getSize() / 100.0));
        var coordinates = positioner.defineXY(attr.getPosition(), image.getWidth(), image.getHeight(), watermarkWidth, watermarkHeight);
        coordinates.forEach(v->drawWatermark(g2d, watermarkImage, v.getX(), v.getY(), watermarkWidth, watermarkHeight));
    }

    private void configureGraphics(Graphics2D g2d, AlphaComposite alphaChannel) {
        g2d.setComposite(alphaChannel);
    }

    private void drawWatermark(Graphics2D g2d, BufferedImage watermarkImage, int x, int y, int width, int height) {
        g2d.drawImage(watermarkImage, x, y, width, height, null);
    }
}