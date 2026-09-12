package com.markit.audio.tts;

import com.markit.servicelocator.Prioritizable;

/**
 * SPI for offline conversion of plain text to WAV audio.
 *
 * @since 1.5.0
 */
public interface TextToSpeechEngine extends Prioritizable {

    /**
     * Synthesizes text into a complete WAV file.
     *
     * @param text non-blank text
     * @return encoded WAV bytes
     * @throws Exception if synthesis fails
     */
    byte[] synthesize(String text) throws Exception;

    /**
     * Synthesizes text using an explicitly selected voice.
     *
     * <p>This default method preserves compatibility with engines written before
     * voice selection was introduced. Such engines continue to support the
     * default voice through {@link #synthesize(String)} and reject explicit
     * voice selection until they override this method.</p>
     *
     * @param text non-blank text
     * @param voice voice supported by this engine
     * @return encoded WAV bytes
     * @throws Exception if synthesis fails
     */
    default byte[] synthesize(String text, TextToSpeechVoice voice) throws Exception {
        throw new UnsupportedOperationException(
                "Text-to-speech engine " + getClass().getName() + " does not support voice selection");
    }
}
