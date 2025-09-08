package com.markit.image;

import com.markit.api.WatermarkAttributes;
import com.markit.servicelocator.Prioritizable;

import java.awt.Graphics2D;

/**
 * The interface for image-based watermark painting
 *
 * @author Oleg Cheban
 * @since 1.3.5
 */
public interface ImageBasedWatermarkPainter extends Prioritizable {

    void draw(Graphics2D g2d, WatermarkAttributes attr);
}
