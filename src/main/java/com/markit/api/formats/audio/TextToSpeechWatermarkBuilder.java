package com.markit.api.formats.audio;

import com.markit.audio.tts.TextToSpeechVoice;

/**
 * Speech-specific configuration stage for an audible text watermark.
 *
 * @since 1.5.0
 */
public interface TextToSpeechWatermarkBuilder {

    /**
     * Selects a voice supported by the active text-to-speech engine.
     * Providers expose their available voices as typed constants, such as
     * {@link com.markit.audio.tts.FreeTtsVoice#KEVIN_16} for the bundled engine.
     *
     * @param voice voice supported by the active engine
     * @return this speech configuration stage
     */
    TextToSpeechWatermarkBuilder voice(TextToSpeechVoice voice);

    /** Finishes speech configuration and returns to audio mixing controls. */
    WatermarkAudioBuilder end();
}
