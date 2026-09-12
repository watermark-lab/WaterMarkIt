package com.markit.audio.tts;

/**
 * Identifies a voice supported by a {@link TextToSpeechEngine}.
 *
 * <p>TTS providers should expose their supported voices as an enum implementing
 * this interface. This gives callers discoverable, compile-time-safe constants
 * without coupling the audio watermark DSL to one speech engine.</p>
 *
 * @since 1.5.0
 */
public interface TextToSpeechVoice {
}
