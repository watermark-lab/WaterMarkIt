package com.markit.video.ffmpeg.filters;

import com.markit.api.WatermarkAttributes;
import com.markit.api.positioning.Coordinates;
import com.markit.image.WatermarkPositioner;
import com.markit.video.ffmpeg.probes.VideoDimensions;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

/**
 * Appends image overlay ({@code overlay}) steps to an ffmpeg filter graph.
 *
 * @author Oleg Cheban
 * @since 1.4.0
 */
public class OverlayFilterStepBuilder implements FilterStepBuilder {

    @Override
    public FilterStepType getFilterStepType() {
        return FilterStepType.OVERLAY;
    }

    @Override
    public void appendTo(FilterGraph graph, List<WatermarkAttributes> attrs, VideoDimensions dimensions) throws IOException {
        for (WatermarkAttributes attr : attrs) {
            BufferedImage overlay = prepareOverlay(attr);
            for (Coordinates coord : place(attr, dimensions, overlay)) {
                graph.appendOverlay(OverlayImages.writeTempPng(overlay), coord);
            }
        }
    }

    private BufferedImage prepareOverlay(WatermarkAttributes attr) {
        BufferedImage scaled = scaleToSize(attr.getImage().get(), attr.getSize());
        BufferedImage withOpacity = OverlayImages.applyOpacity(scaled, attr.getOpacityFraction());
        return OverlayImages.rotate(withOpacity, attr.getRotationDegrees());
    }

    private BufferedImage scaleToSize(BufferedImage original, int sizePercentage) {
        double scale = sizePercentage / 100.0;
        int width = Math.max(1, (int) Math.round(original.getWidth() * scale));
        int height = Math.max(1, (int) Math.round(original.getHeight() * scale));

        BufferedImage scaled = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = scaled.createGraphics();
        try {
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.drawImage(original, 0, 0, width, height, null);
        } finally {
            g2d.dispose();
        }
        return scaled;
    }

    private List<Coordinates> place(WatermarkAttributes attr, VideoDimensions dimensions, BufferedImage overlay) {
        return WatermarkPositioner.defineXY(
                attr,
                dimensions.getWidth(),
                dimensions.getHeight(),
                overlay.getWidth(),
                overlay.getHeight()
        );
    }

    @Override
    public int getPriority() {
        return DEFAULT_PRIORITY;
    }
}
