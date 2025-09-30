package com.markit.video.ffmpeg;

import com.markit.servicelocator.Prioritizable;
import com.markit.video.ffmpeg.filters.FilterResult;

import java.io.File;

/**
 *
 * @author Oleg Cheban
 * @since 1.4.0
 */
public interface CommandExecutor extends Prioritizable {
    byte[] execute(File input, FilterResult data) throws Exception;
}
