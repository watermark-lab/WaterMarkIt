package com.markit.video.ffmpeg.filters;

import com.markit.api.WatermarkAttributes;
import com.markit.servicelocator.Prioritizable;
import com.markit.video.ffmpeg.probes.VideoDimensions;

import java.util.List;

/**
 *
 * @author Oleg Cheban
 * @since 1.4.0
 */
public interface FilterStepBuilder extends Prioritizable {

    FilterStepAttributes build(List<WatermarkAttributes> attrs, VideoDimensions dimensions, String lastLabel, int step, boolean isEmptyFilter) throws Exception;

    FilterStepType getFilterStepType();
}
