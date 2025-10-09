package com.markit.image;

import com.markit.api.WatermarkAttributes;
import com.markit.servicelocator.Prioritizable;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * The interface for text-based watermark painting
 *
 * @author Oleg Cheban
 * @since 1.3.5
 */
public interface TextBasedWatermarkPainter extends Prioritizable {

    void draw(Graphics2D g2d, BufferedImage sourceImage, WatermarkAttributes attr);
}
