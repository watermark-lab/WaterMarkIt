package com.markit.video.ffmpeg.filters;

import com.markit.api.WatermarkAttributes;
import com.markit.video.ffmpeg.probes.VideoDimensions;
import com.markit.video.ffmpeg.probes.VideoInfoExtractor;

import java.io.File;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Default {@link FilterChainBuilder} that assembles a text step followed by an
 * image overlay step into a single ffmpeg filter graph.
 *
 * @author Oleg Cheban
 * @since 1.4.0
 */
public class DefaultFilterChainBuilder implements FilterChainBuilder {

    @Override
    public FilterResult build(File video, List<WatermarkAttributes> attributes) throws Exception {
        VideoDimensions dimensions = VideoInfoExtractor.getVideoDimensions(video);
        FilterGraph graph = new FilterGraph();

        appendStep(graph, FilterStepType.DRAWTEXT, dimensions, select(attributes, WatermarkAttributes::isTextWatermark));
        appendStep(graph, FilterStepType.OVERLAY, dimensions, select(attributes, WatermarkAttributes::isImageWatermark));

        return graph.toResult();
    }

    private void appendStep(FilterGraph graph, FilterStepType type, VideoDimensions dimensions,
                            List<WatermarkAttributes> attrs) throws Exception {
        if (attrs.isEmpty()) {
            return;
        }
        FilterStepBuilderFactory.getInstance().getBuilder(type).appendTo(graph, attrs, dimensions);
    }

    private List<WatermarkAttributes> select(List<WatermarkAttributes> attributes,
                                             Predicate<WatermarkAttributes> filter) {
        return attributes.stream().filter(filter).collect(Collectors.toList());
    }

    @Override
    public int getPriority() {
        return DEFAULT_PRIORITY;
    }
}
