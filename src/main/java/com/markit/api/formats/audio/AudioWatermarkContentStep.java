package com.markit.api.formats.audio;

import java.io.File;

/**
 * Staged DSL for adding audible watermark layers to audio.
 *
 * @since 1.5.0
 */
public interface AudioWatermarkContentStep {

    /** Selects an encoded audio file as the current watermark layer. */
    WatermarkAudioBuilder withAudio(File watermark);

    /** Selects encoded audio bytes as the current watermark layer. */
    WatermarkAudioBuilder withAudio(byte[] watermark);

    /** Selects text that will be synthesized locally as the current watermark layer. */
    TextToSpeechWatermarkBuilder withText(String text);
}
