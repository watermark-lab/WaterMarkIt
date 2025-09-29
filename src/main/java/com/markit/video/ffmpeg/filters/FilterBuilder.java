package com.markit.video.ffmpeg.filters;

import com.markit.api.WatermarkAttributes;
import com.markit.video.ffmpeg.probes.VideoDimensions;
import com.markit.video.ffmpeg.probes.VideoInfoExtractor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Oleg Cheban
 * @since 1.4.0
 */
public class FilterBuilder {

    public FilterResult build(File video, List<WatermarkAttributes> attributes) throws Exception {
        StringBuilder filter = new StringBuilder();
        List<File> tempImages = new ArrayList<>();
        String lastLabel = "[0:v]";
        int step = 0;
        boolean isEmptyFilter = true;

        VideoDimensions dimensions = VideoInfoExtractor.getVideoDimensions(video);

        // Build text filters
        List<WatermarkAttributes> textAttributes = getTextAttributes(attributes);
        if (!textAttributes.isEmpty()) {
            FilterStepBuilder textBuilder = FilterStepBuilderFactory.getInstance().getBuilder(Step.TEXT);
            FilterStep textStep = textBuilder.build(textAttributes, dimensions, lastLabel, step, isEmptyFilter);
            filter.append(textStep.getFilter());
            lastLabel = textStep.getLastLabel();
            step = textStep.getStep();
            isEmptyFilter = textStep.getEmpty();
        }

        // Build image overlays
        List<WatermarkAttributes> imageAttributes = getImageAttributes(attributes);
        if (!imageAttributes.isEmpty()) {
            FilterStepBuilder overlayBuilder = FilterStepBuilderFactory.getInstance().getBuilder(Step.OVERLAY);
            FilterStep imageStep = overlayBuilder.build(imageAttributes, dimensions, lastLabel, step, isEmptyFilter);
            filter.append(imageStep.getFilter());
            tempImages.addAll(imageStep.getTempImages());
            lastLabel = imageStep.getLastLabel();
        }

        return new FilterResult(filter.toString(), lastLabel, tempImages);
    }

    private List<WatermarkAttributes> getTextAttributes(List<WatermarkAttributes> attributes) {
        List<WatermarkAttributes> textAttrs = new ArrayList<>();
        for (WatermarkAttributes attr : attributes) {
            if (attr.isTextWatermark()) {
                textAttrs.add(attr);
            }
        }
        return textAttrs;
    }

    private List<WatermarkAttributes> getImageAttributes(List<WatermarkAttributes> attributes) {
        List<WatermarkAttributes> imageAttrs = new ArrayList<>();
        for (WatermarkAttributes attr : attributes) {
            if (attr.isImageWatermark()) {
                imageAttrs.add(attr);
            }
        }
        return imageAttrs;
    }
}
