package com.markit.video.ffmpeg.filters;

import com.markit.api.WatermarkAttributes;
import com.markit.video.ffmpeg.image.ImageOverlayBuilder;
import com.markit.video.ffmpeg.probes.VideoDimensions;
import com.markit.video.ffmpeg.probes.VideoInfoExtractor;
import com.markit.video.ffmpeg.text.TextFilterBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FilterBuilder {

    private final TextFilterBuilder textBuilder = new TextFilterBuilder();
    private final ImageOverlayBuilder imageBuilder = new ImageOverlayBuilder();

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
            FilterStep textStep = textBuilder.build(textAttributes, lastLabel, step, isEmptyFilter);
            filter.append(textStep.getFilter());
            lastLabel = textStep.getLastLabel();
            step = textStep.getStep();
            isEmptyFilter = textStep.getEmpty();
        }

        // Build image overlays
        List<WatermarkAttributes> imageAttributes = getImageAttributes(attributes);
        if (!imageAttributes.isEmpty()) {
            FilterStep imageStep = imageBuilder.build(imageAttributes, dimensions, lastLabel, step, isEmptyFilter);
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
