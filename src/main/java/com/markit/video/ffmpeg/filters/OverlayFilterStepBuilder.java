package com.markit.video.ffmpeg.filters;

import com.markit.api.WatermarkAttributes;
import com.markit.api.positioning.Coordinates;
import com.markit.image.WatermarkPositioner;
import com.markit.video.ffmpeg.probes.VideoDimensions;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Oleg Cheban
 * @since 1.4.0
 */
public class OverlayFilterStepBuilder implements FilterStepBuilder {

    public FilterStepAttributes build(List<WatermarkAttributes> attrs, VideoDimensions dimensions, String lastLabel, int step, boolean isEmptyFilter) throws Exception {
        StringBuilder filter = new StringBuilder();
        List<File> tempImages = new ArrayList<>();

        for (WatermarkAttributes a : attrs) {
            BufferedImage original = a.getImage().get();
            int targetWidth = Math.max(1, (int) Math.round(original.getWidth() * (a.getSize() / 100.0)));
            int targetHeight = Math.max(1, (int) Math.round(original.getHeight() * (a.getSize() / 100.0)));

            BufferedImage scaled = scaleImage(original, targetWidth, targetHeight);

            List<Coordinates> coordinates =
                    WatermarkPositioner.defineXY(a, dimensions.getWidth(), dimensions.getHeight(), targetWidth, targetHeight);

            for (Coordinates c : coordinates) {
                File img = Files.createTempFile("wmk-img", ".png").toFile();
                ImageIO.write(scaled, "png", img);
                tempImages.add(img);

                String inLabel = step == 0 ? "[0:v]" : lastLabel;
                String outLabel = "[v" + (step + 1) + "]";

                String overlay = String.format("%s[%d:v]overlay=x=%d:y=%d%s",
                        inLabel, tempImages.size(), c.getX(), c.getY(), outLabel);

                if (!isEmptyFilter) filter.append(",");
                filter.append(overlay);

                lastLabel = outLabel;
                isEmptyFilter = false;
                step++;
            }
        }

        return new FilterStepAttributes(filter.toString(), lastLabel, step, isEmptyFilter, tempImages);
    }

    private BufferedImage scaleImage(BufferedImage original, int targetWidth, int targetHeight) {
        BufferedImage scaled = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = scaled.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.drawImage(original, 0, 0, targetWidth, targetHeight, null);
        g2d.dispose();
        return scaled;
    }

    @Override
    public int getPriority() {
        return DEFAULT_PRIORITY;
    }

    @Override
    public StepType getStepType() {
        return StepType.OVERLAY;
    }
}