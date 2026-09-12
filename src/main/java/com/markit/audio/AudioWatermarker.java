package com.markit.audio;

import com.markit.api.AudioWatermarkAttributes;
import com.markit.servicelocator.Prioritizable;

import java.io.File;
import java.util.List;

/**
 * SPI for mixing audible watermark layers into source audio.
 *
 * @since 1.5.0
 */
public interface AudioWatermarker extends Prioritizable {

    byte[] watermark(byte[] sourceAudio, List<AudioWatermarkAttributes> watermarks) throws Exception;

    byte[] watermark(File sourceAudio, List<AudioWatermarkAttributes> watermarks) throws Exception;
}
