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
 * drawtext filter chain builder
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
    public FilterStepAttributes build(List<WatermarkAttributes> attrs, VideoDimensions dimensions,
                                      String lastLabel, int step, boolean isEmptyFilter) {
        StringBuilder filter = new StringBuilder();
        Graphics2D g2d = createGraphicsContext(dimensions);

        try {
            for (WatermarkAttributes attr : attrs) {
                FilterBuildContext context = processWatermark(attr, dimensions, g2d, lastLabel, step, isEmptyFilter);
                appendFilters(filter, context);

                lastLabel = context.lastLabel;
                step = context.step;
                isEmptyFilter = context.isEmptyFilter;
            }
        } finally {
            g2d.dispose();
        }

        return new FilterStepAttributes(filter.toString(), lastLabel, step, isEmptyFilter);
    }

    private Graphics2D createGraphicsContext(VideoDimensions dimensions) {
        BufferedImage tempImage = new BufferedImage(
                dimensions.getWidth(),
                dimensions.getHeight(),
                BufferedImage.TYPE_INT_ARGB
        );
        return tempImage.createGraphics();
    }

    private FilterBuildContext processWatermark(WatermarkAttributes attr, VideoDimensions dimensions,
                                                Graphics2D g2d, String lastLabel, int step, boolean isEmptyFilter) {
        Font font = createFont(attr);
        Rectangle2D textBounds = calculateTextBounds(attr.getText(), font, g2d.getFontRenderContext());
        List<Coordinates> coordinates = calculateCoordinates(attr, dimensions, textBounds);

        return new FilterBuildContext(coordinates, attr, lastLabel, step, isEmptyFilter);
    }

    private Font createFont(WatermarkAttributes attr) {
        int fontStyle = attr.isBold() ? Font.BOLD : Font.PLAIN;
        return new Font(attr.getFont().getAwtFontName(), fontStyle, attr.getSize());
    }

    private Rectangle2D calculateTextBounds(String text, Font font, FontRenderContext frc) {
        TextLayout layout = new TextLayout(text, font, frc);
        return layout.getBounds();
    }

    private List<Coordinates> calculateCoordinates(WatermarkAttributes attr, VideoDimensions dimensions,
                                                   Rectangle2D textBounds) {
        return WatermarkPositioner.defineXY(
                attr,
                dimensions.getWidth(),
                dimensions.getHeight(),
                (int) textBounds.getWidth(),
                (int) textBounds.getHeight()
        );
    }

    private void appendFilters(StringBuilder filter, FilterBuildContext context) {
        for (Coordinates coord : context.coordinates) {
            String drawtextFilter = buildDrawtextFilter(context.attr, coord, context.inLabel, context.outLabel);

            if (!context.isEmptyFilter) {
                filter.append(",");
            }
            filter.append(drawtextFilter);

            context.advance();
        }
    }

    private String buildDrawtextFilter(WatermarkAttributes attr, Coordinates coord, String inLabel, String outLabel) {
        return String.format(
                "%sdrawtext=text='%s':fontcolor=%s@%s:fontsize=%d:x=%s:y=%s%s",
                inLabel,
                attr.getText(),
                getColorValue(attr.getColor()),
                getOpacityValue(attr.getOpacity()),
                attr.getSize(),
                coord.getX(),
                coord.getY(),
                outLabel
        );
    }

    /**
     * method converts a Java Color object to ffmpeg's expected hexadecimal color format
     */
    private String getColorValue(Color color) {
        return String.format("%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
    }

    /**
     * method converts a percentage-based opacity (0-100) to FFmpeg's decimal format (0.0-1.0).
     */
    private float getOpacityValue(int opacity) {
        return Math.max(0, Math.min(100, opacity)) / 100f;
    }

    @Override
    public int getPriority() {
        return DEFAULT_PRIORITY;
    }

    /**
     * Context holder for filter building state
     */
    private static class FilterBuildContext {
        private final List<Coordinates> coordinates;
        private final WatermarkAttributes attr;
        private String lastLabel;
        private int step;
        private boolean isEmptyFilter;
        private String inLabel;
        private String outLabel;

        FilterBuildContext(List<Coordinates> coordinates, WatermarkAttributes attr,
                           String lastLabel, int step, boolean isEmptyFilter) {
            this.coordinates = coordinates;
            this.attr = attr;
            this.lastLabel = lastLabel;
            this.step = step;
            this.isEmptyFilter = isEmptyFilter;
            updateLabels();
        }

        private void updateLabels() {
            this.inLabel = step == 0 ? "[0:v]" : lastLabel;
            this.outLabel = "[v" + (step + 1) + "]";
        }

        void advance() {
            this.lastLabel = this.outLabel;
            this.isEmptyFilter = false;
            this.step++;
            updateLabels();
        }
    }
}