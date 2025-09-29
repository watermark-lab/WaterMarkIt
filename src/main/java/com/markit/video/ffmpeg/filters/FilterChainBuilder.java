package com.markit.video.ffmpeg.filters;

import com.markit.api.WatermarkAttributes;
import com.markit.servicelocator.Prioritizable;

import java.io.File;
import java.util.List;

/**
 *
 * @author Oleg Cheban
 * @since 1.4.0
 */
public interface FilterChainBuilder extends Prioritizable {

    FilterResult build(File video, List<WatermarkAttributes> attributes) throws Exception;
}
