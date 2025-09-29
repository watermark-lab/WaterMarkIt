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
 *
 * @author Oleg Cheban
 * @since 1.4.0
 */
public class TextFilterStepBuilder implements FilterStepBuilder {

    @Override
    public FilterStepAttributes build(List<WatermarkAttributes> attrs, VideoDimensions dimensions, String lastLabel, int step, boolean isEmptyFilter) {
        StringBuilder filter = new StringBuilder();

        BufferedImage tempImage = new BufferedImage(dimensions.getWidth(), dimensions.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = tempImage.createGraphics();

        for (WatermarkAttributes a : attrs) {
            FontRenderContext frc = g2d.getFontRenderContext();
            var fontSize = calculateFontSize(a.getSize(), tempImage.getWidth(), tempImage.getHeight());
            var fontStyle = a.isBold() ? Font.BOLD : Font.PLAIN;;
            var font = new Font(a.getFont().getAwtFontName(), fontStyle, fontSize);
            TextLayout watermarkLayout = new TextLayout(a.getText(), font, frc);
            Rectangle2D rect = watermarkLayout.getBounds();

            var coordinates =
                    WatermarkPositioner.defineXY(
                            a, dimensions.getWidth(), dimensions.getHeight(), (int) rect.getWidth(), (int) rect.getHeight());

            for (Coordinates c : coordinates) {
                String inLabel = step == 0 ? "[0:v]" : lastLabel;
                String outLabel = "[v" + (step + 1) + "]";

                String drawtext = String.format(
                        "%sdrawtext=text='%s':fontcolor=%s@%s:fontsize=%d:x=%s:y=%s%s",
                        inLabel,
                        a.getText(),
                        getColorValue(a.getColor()),
                        getOpacityValue(a.getOpacity()),
                        a.getSize(),
                        c.getX(),
                        c.getY(),
                        outLabel
                );

                if (!isEmptyFilter) filter.append(",");
                filter.append(drawtext);

                lastLabel = outLabel;
                isEmptyFilter = false;
                step++;
            }
        }

        return new FilterStepAttributes(filter.toString(), lastLabel, step, isEmptyFilter);
    }

    private int calculateFontSize(int textSize, int imageWidth, int imageHeight) {
        if (textSize > 0) return textSize;
        return Math.min(imageWidth, imageHeight) / 10;
    }

    private String getColorValue(Color color) {
        return String.format("%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
    }

    private float getOpacityValue(int opacity) {
        return Math.max(0, Math.min(100, opacity)) / 100f;
    }

    @Override
    public int getPriority() {
        return DEFAULT_PRIORITY;
    }

    @Override
    public StepType getStepType() {
        return StepType.TEXT;
    }
}
