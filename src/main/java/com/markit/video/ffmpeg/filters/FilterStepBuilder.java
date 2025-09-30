package com.markit.video.ffmpeg.filters;

import com.markit.api.WatermarkAttributes;
import com.markit.servicelocator.Prioritizable;
import com.markit.video.ffmpeg.probes.VideoDimensions;

import java.util.List;

/**
 * Builds a single step of an ffmpeg filter graph used for video watermarking.
 * <p>
 * Each implementation handles a particular {@link FilterStepType} (e.g., text drawing
 * or image overlay) and produces the partial filter string, the updated last label,
 * and any temporary resources required for the step.
 * </p>
 *
 * <p>
 * Steps are typically chained by a {@link FilterChainBuilder}, which passes the
 * {@code lastLabel} from the previous step into the next one.
 * </p>
 *
 * @author Oleg Cheban
 * @since 1.4.0
 */
public interface FilterStepBuilder extends Prioritizable {

    /**
     * Build this step's contribution to the filter graph.
     *
     * @param attrs watermark attributes relevant to this step
     * @param dimensions probed video dimensions for positioning and scaling
     * @param lastLabel the label of the previous step's output (or input stream label)
     * @param step sequential index of this step in the overall chain
     * @param isEmptyFilter whether the chain has not added any filters yet (first step)
     * @return {@link FilterStepAttributes} describing this step's filter fragment and state
     * @throws Exception if the step cannot be constructed (invalid params, IO issues, etc.)
     */
    FilterStepAttributes build(List<WatermarkAttributes> attrs, VideoDimensions dimensions, String lastLabel, int step, boolean isEmptyFilter) throws Exception;

    FilterStepType getFilterStepType();
}
