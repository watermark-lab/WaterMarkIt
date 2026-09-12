package com.markit.api.formats.audio;

import com.markit.api.AudioWatermarkAttributes;

import java.util.List;

/** Executes one fully configured audio watermark operation. */
@FunctionalInterface
interface AudioWatermarkProcessor {
    byte[] apply(List<AudioWatermarkAttributes> watermarks) throws Exception;
}
