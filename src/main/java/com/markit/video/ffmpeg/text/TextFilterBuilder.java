package com.markit.video.ffmpeg.text;

import com.markit.api.WatermarkAttributes;
import com.markit.video.ffmpeg.filters.FilterStep;

import java.awt.*;
import java.util.List;

public class TextFilterBuilder {

    public FilterStep build(List<WatermarkAttributes> attrs, String lastLabel, int step, boolean isEmptyFilter) {
        StringBuilder filter = new StringBuilder();

        for (WatermarkAttributes a : attrs) {
            String inLabel = step == 0 ? "[0:v]" : lastLabel;
            String outLabel = "[v" + (step + 1) + "]";

            Coordinates cr = WatermarkPositionHelper.calculateWatermarkPosition(
                    a.getPosition(),
                    a.getCustomCoordinates(),
                    String.valueOf(a.getPositionCoordinates().getX()),
                    String.valueOf(a.getPositionCoordinates().getY()));

            String drawtext = String.format(
                    "%sdrawtext=text='%s':fontcolor=%s@%s:fontsize=%d:x=%s:y=%s%s",
                    inLabel,
                    a.getText(),
                    getColorValue(a.getColor()),
                    getOpacityValue(a.getOpacity()),
                    a.getSize(),
                    cr.getX(),
                    cr.getY(),
                    outLabel
            );

            if (!isEmptyFilter) filter.append(",");
            filter.append(drawtext);

            lastLabel = outLabel;
            isEmptyFilter = false;
            step++;
        }

        return new FilterStep(filter.toString(), lastLabel, step, isEmptyFilter);
    }

    private String getColorValue(Color color) {
        return String.format("%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
    }

    private float getOpacityValue(int opacity) {
        return Math.max(0, Math.min(100, opacity)) / 100f;
    }
}
