package com.markit.video.ffmpeg.filters;

import com.markit.api.WatermarkAttributes;
import com.markit.api.positioning.Coordinates;
import com.markit.image.WatermarkPositioner;
import com.markit.video.ffmpeg.probes.VideoDimensions;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 * Appends text ({@code drawtext}) steps to an ffmpeg filter graph.
 *
 * @author Oleg Cheban
 * @since 1.4.0
 */
public class TextFilterStepBuilder implements FilterStepBuilder {

    @Override
    public FilterStepType getFilterStepType() {
        return FilterStepType.DRAWTEXT;
    }

    @Override
    public void appendTo(FilterGraph graph, List<WatermarkAttributes> attrs, VideoDimensions dimensions) {
        Graphics2D g2d = createGraphicsContext(dimensions);
        try {
            for (WatermarkAttributes attr : attrs) {
                Rectangle2D bounds = textBounds(attr, g2d.getFontRenderContext());
                for (Coordinates coord : place(attr, dimensions, bounds)) {
                    graph.append((in, out) -> drawtextFilter(attr, coord, in, out));
                }
            }
        } finally {
            g2d.dispose();
        }
    }

    private Graphics2D createGraphicsContext(VideoDimensions dimensions) {
        BufferedImage tempImage = new BufferedImage(
                dimensions.getWidth(),
                dimensions.getHeight(),
                BufferedImage.TYPE_INT_ARGB
        );
        return tempImage.createGraphics();
    }

    private Rectangle2D textBounds(WatermarkAttributes attr, FontRenderContext frc) {
        int fontStyle = attr.isBold() ? Font.BOLD : Font.PLAIN;
        Font font = new Font(attr.getFont().getAwtFontName(), fontStyle, attr.getSize());
        return new TextLayout(attr.getText(), font, frc).getBounds();
    }

    private List<Coordinates> place(WatermarkAttributes attr, VideoDimensions dimensions, Rectangle2D bounds) {
        return WatermarkPositioner.defineXY(
                attr,
                dimensions.getWidth(),
                dimensions.getHeight(),
                (int) bounds.getWidth(),
                (int) bounds.getHeight()
        );
    }

    private String drawtextFilter(WatermarkAttributes attr, Coordinates coord, String inLabel, String outLabel) {
        return String.format(
                "%sdrawtext=text='%s':fontcolor=%s@%s:fontsize=%d:x=%s:y=%s%s",
                inLabel,
                attr.getText(),
                toHexColor(attr.getColor()),
                attr.getOpacityFraction(),
                attr.getSize(),
                coord.getX(),
                coord.getY(),
                outLabel
        );
    }

    /**
     * Converts a Java {@link Color} to ffmpeg's expected hexadecimal color format.
     */
    private String toHexColor(Color color) {
        return String.format("%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
    }

    @Override
    public int getPriority() {
        return DEFAULT_PRIORITY;
    }
}
