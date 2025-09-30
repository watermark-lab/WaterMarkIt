package com.markit.video.ffmpeg.filters;

import com.markit.api.WatermarkAttributes;
import com.markit.servicelocator.Prioritizable;

import java.io.File;
import java.util.List;

/**
 * Builds a complete ffmpeg video filter chain for watermarking.
 * <p>
 * Implementations translate high-level {@link com.markit.api.WatermarkAttributes}
 * into a concrete ffmpeg filter graph string and auxiliary data (e.g., temp
 * image files) represented by {@link FilterResult}. The resulting filter chain
 * can then be executed by a {@link com.markit.video.ffmpeg.CommandExecutor}.
 * </p>
 *
 * <p>
 * Implementations may inspect the source {@link java.io.File} and attributes
 * to decide which {@link FilterStepBuilder} steps to include (e.g. drawtext,
 * overlay) and how to connect labels between steps.
 * </p>
 *
 * @author Oleg Cheban
 * @since 1.4.0
 */
public interface FilterChainBuilder extends Prioritizable {

    /**
     * Build a runnable filter graph for the provided video and watermark instructions.
     *
     * @param video the input video file to watermark
     * @param attributes ordered list of high-level watermark attributes to apply
     * @return a {@link FilterResult} containing the ffmpeg filter string, the last label
     *         to map from, and any temporary image files that must be cleaned up
     * @throws Exception if the chain cannot be constructed (invalid attributes, IO issues, etc.)
     */
    FilterResult build(File video, List<WatermarkAttributes> attributes) throws Exception;
}
