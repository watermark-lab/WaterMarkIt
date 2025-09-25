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

    public FilterResult build(File video, List<WatermarkAttributes> attrs) throws Exception {
        StringBuilder filter = new StringBuilder();
        List<File> tempImages = new ArrayList<>();
        String lastLabel = "[0:v]";
        int step = 0;
        boolean isEmpty = true;

        VideoDimensions dimensions = VideoInfoExtractor.getVideoDimensions(video);

        // Build text filters
        FilterStep textStep = textBuilder.build(attrs, lastLabel, step, isEmpty);
        filter.append(textStep.getFilter());
        lastLabel = textStep.getLastLabel();
        step = textStep.getStep();
        isEmpty = textStep.getEmpty();

        // Build image overlays
        FilterStep imageStep = imageBuilder.build(attrs, dimensions, lastLabel, step, isEmpty);
        filter.append(imageStep.getFilter());
        tempImages.addAll(imageStep.getTempImages());
        lastLabel = imageStep.getLastLabel();

        return new FilterResult(filter.toString(), lastLabel, tempImages);
    }
}
