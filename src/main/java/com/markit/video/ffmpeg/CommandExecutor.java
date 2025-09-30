package com.markit.video.ffmpeg;

import com.markit.servicelocator.Prioritizable;
import com.markit.video.ffmpeg.filters.FilterResult;

import java.io.File;

/**
 * Executes an ffmpeg command to apply the constructed filter chain to a video.
 * <p>
 * Implementations are responsible for invoking the ffmpeg binary (or library bindings),
 * wiring inputs, mapping the last label from {@link com.markit.video.ffmpeg.filters.FilterResult},
 * and returning the processed video bytes.
 * </p>
 *
 * <p>
 * Implementations may also manage temporary files produced during chain building.
 * </p>
 *
 * @author Oleg Cheban
 * @since 1.4.0
 */
public interface CommandExecutor extends Prioritizable {
    /**
     * Execute the ffmpeg process with the provided filter graph.
     *
     * @param input the input video file to process
     * @param data the filter graph, last label, and any temporary resources
     * @return the resulting video as a byte array
     * @throws Exception if execution fails or ffmpeg returns a non-zero exit code
     */
    byte[] execute(File input, FilterResult data) throws Exception;
}
