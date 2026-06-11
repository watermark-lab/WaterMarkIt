package com.markit.video.ffmpeg.filters;

import com.markit.api.WatermarkAttributes;
import com.markit.servicelocator.Prioritizable;
import com.markit.video.ffmpeg.probes.VideoDimensions;

import java.util.List;

/**
 * Appends one kind of watermark step (text or image overlay) to an ffmpeg filter graph.
 * <p>
 * Each implementation handles a particular {@link FilterStepType} and appends its
 * filter fragments to the shared {@link FilterGraph}, which manages label chaining
 * between steps and collects any temporary resources.
 * </p>
 *
 * <p>
 * Steps are orchestrated by a {@link FilterChainBuilder}, which appends each step
 * to the same {@link FilterGraph} in turn.
 * </p>
 *
 * @author Oleg Cheban
 * @since 1.4.0
 */
public interface FilterStepBuilder extends Prioritizable {

    /**
     * Append this step's fragments to the filter graph.
     *
     * @param graph the shared filter graph to append to (mutated in place)
     * @param attrs watermark attributes relevant to this step
     * @param dimensions probed video dimensions for positioning and scaling
     * @throws Exception if the step cannot be constructed (invalid params, IO issues, etc.)
     */
    void appendTo(FilterGraph graph, List<WatermarkAttributes> attrs, VideoDimensions dimensions) throws Exception;

    FilterStepType getFilterStepType();
}
