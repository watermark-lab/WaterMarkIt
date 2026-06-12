package com.markit.video.ffmpeg.filters;

import com.markit.api.WatermarkAttributes;
import com.markit.api.positioning.Coordinates;
import com.markit.image.WatermarkPositioner;
import com.markit.video.ffmpeg.probes.VideoDimensions;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

/**
 * Appends text steps to an ffmpeg filter graph.
 * <p>
 * ffmpeg's {@code drawtext} renders text natively but cannot rotate it, so it is used only for
 * upright text. Rotated text is instead rendered to a transparent image and composited as an
 * overlay, the same way image watermarks are handled.
 * </p>
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
    public void appendTo(FilterGraph graph, List<WatermarkAttributes> attrs, VideoDimensions dimensions) throws IOException {
        for (WatermarkAttributes attr : attrs) {
            if (attr.getRotationDegrees() == 0) {
                appendDrawText(graph, attr, dimensions);
            } else {
                appendRotatedText(graph, attr, dimensions);
            }
        }
    }

    private void appendDrawText(FilterGraph graph, WatermarkAttributes attr, VideoDimensions dimensions) {
        Rectangle2D bounds = textBounds(attr);
        for (Coordinates coord : place(attr, dimensions, (int) bounds.getWidth(), (int) bounds.getHeight())) {
            graph.append((in, out) -> drawtextFilter(attr, coord, in, out));
        }
    }

    private void appendRotatedText(FilterGraph graph, WatermarkAttributes attr, VideoDimensions dimensions) throws IOException {
        BufferedImage textImage = OverlayImages.rotate(
                OverlayImages.applyOpacity(renderText(attr), attr.getOpacityFraction()),
                attr.getRotationDegrees()
        );
        for (Coordinates coord : place(attr, dimensions, textImage.getWidth(), textImage.getHeight())) {
            graph.appendOverlay(OverlayImages.writeTempPng(textImage), coord);
        }
    }

    private BufferedImage renderText(WatermarkAttributes attr) {
        Rectangle2D bounds = textBounds(attr);
        int width = Math.max(1, (int) Math.ceil(bounds.getWidth()));
        int height = Math.max(1, (int) Math.ceil(bounds.getHeight()));

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        try {
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2d.setColor(attr.getColor());
            new TextLayout(attr.getText(), font(attr), g2d.getFontRenderContext())
                    .draw(g2d, (float) -bounds.getX(), (float) -bounds.getY());
        } finally {
            g2d.dispose();
        }
        return image;
    }

    private Rectangle2D textBounds(WatermarkAttributes attr) {
        BufferedImage probe = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = probe.createGraphics();
        try {
            return new TextLayout(attr.getText(), font(attr), g2d.getFontRenderContext()).getBounds();
        } finally {
            g2d.dispose();
        }
    }

    private Font font(WatermarkAttributes attr) {
        int fontStyle = attr.isBold() ? Font.BOLD : Font.PLAIN;
        return new Font(attr.getFont().getAwtFontName(), fontStyle, attr.getSize());
    }

    private List<Coordinates> place(WatermarkAttributes attr, VideoDimensions dimensions, int width, int height) {
        return WatermarkPositioner.defineXY(attr, dimensions.getWidth(), dimensions.getHeight(), width, height);
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
