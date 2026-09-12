package com.markit.audio.tts;

/**
 * Voices bundled with WaterMarkIt's default FreeTTS installation.
 *
 * @since 1.5.0
 */
public enum FreeTtsVoice implements TextToSpeechVoice {

    /** 8 kHz US-English Kevin voice. */
    KEVIN("kevin"),

    /** 16 kHz US-English Kevin voice. */
    KEVIN_16("kevin16");

    private final String name;

    FreeTtsVoice(String name) {
        this.name = name;
    }

    String getFreeTtsName() {
        return name;
    }
}
