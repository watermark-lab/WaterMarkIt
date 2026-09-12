package com.markit.api.formats.audio;

import java.time.Duration;

/**
 * Mixing controls available after audio watermark content has been selected.
 *
 * @since 1.5.0
 */
public interface WatermarkAudioBuilder {

    /**
     * Sets watermark-layer gain from 0 (silent) to 100 (unity/original gain).
     */
    WatermarkAudioBuilder volume(int volume);

    /** Sets when the watermark starts relative to the source audio. */
    WatermarkAudioBuilder startAt(Duration offset);

    /** Enables or disables this watermark layer. */
    WatermarkAudioBuilder enableIf(boolean condition);

    /** Finishes this layer and starts configuration of another one. */
    AudioWatermarkContentStep and();

    /** Applies all configured audible watermark layers. */
    byte[] apply();
}
