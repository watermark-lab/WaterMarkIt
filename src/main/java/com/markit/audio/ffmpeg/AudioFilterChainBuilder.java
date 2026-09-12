package com.markit.audio.ffmpeg;

import com.markit.api.AudioWatermarkAttributes;
import com.markit.servicelocator.Prioritizable;

import java.util.List;

/** Builds an FFmpeg filter graph for a source and its audible watermark inputs. */
public interface AudioFilterChainBuilder extends Prioritizable {

    AudioFilterResult build(List<AudioWatermarkAttributes> watermarks);
}
