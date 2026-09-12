package com.markit.api;

import com.markit.audio.tts.TextToSpeechVoice;

import java.io.File;
import java.time.Duration;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

/**
 * Immutable description of one audible watermark layer.
 *
 * <p>Each instance has exactly one content source: an audio file, encoded audio
 * bytes, or text that will be synthesized by a text-to-speech engine.</p>
 *
 * @since 1.5.0
 */
public final class AudioWatermarkAttributes {

    /** The kind of content carried by this watermark. */
    public enum ContentType {
        AUDIO_FILE,
        AUDIO_BYTES,
        TEXT
    }

    private final ContentType contentType;
    private final File audioFile;
    private final byte[] audioBytes;
    private final String text;
    private final TextToSpeechVoice voice;
    private final int volume;
    private final Duration startAt;
    private final boolean enabled;

    private AudioWatermarkAttributes(ContentType contentType, File audioFile, byte[] audioBytes,
                                     String text, TextToSpeechVoice voice, int volume,
                                     Duration startAt, boolean enabled) {
        this.contentType = Objects.requireNonNull(contentType, "Content type is required");
        this.audioFile = audioFile;
        this.audioBytes = audioBytes == null ? null : Arrays.copyOf(audioBytes, audioBytes.length);
        this.text = text;
        this.voice = voice;
        this.volume = requireValidVolume(volume);
        this.startAt = requireValidStartAt(startAt);
        this.enabled = enabled;
    }

    /** Creates attributes backed by a readable audio file. */
    public static AudioWatermarkAttributes forAudio(File file, int volume, Duration startAt, boolean enabled) {
        requireReadableFile(file, "Watermark audio file");
        return new AudioWatermarkAttributes(ContentType.AUDIO_FILE, file, null, null, null,
                volume, startAt, enabled);
    }

    /** Creates attributes backed by encoded audio bytes. */
    public static AudioWatermarkAttributes forAudio(byte[] bytes, int volume, Duration startAt, boolean enabled) {
        if (bytes == null) {
            throw new NullPointerException("Watermark audio bytes are required");
        }
        if (bytes.length == 0) {
            throw new IllegalArgumentException("Watermark audio bytes must not be empty");
        }
        return new AudioWatermarkAttributes(ContentType.AUDIO_BYTES, null, bytes, null, null,
                volume, startAt, enabled);
    }

    /** Creates attributes backed by text that will be synthesized locally. */
    public static AudioWatermarkAttributes forText(String text, int volume, Duration startAt, boolean enabled) {
        if (text == null) {
            throw new NullPointerException("Text-to-speech text is required");
        }
        if (text.trim().isEmpty()) {
            throw new IllegalArgumentException("Text-to-speech text must not be empty");
        }
        return new AudioWatermarkAttributes(ContentType.TEXT, null, null, text, null,
                volume, startAt, enabled);
    }

    /** Creates attributes backed by text and an explicitly selected TTS voice. */
    public static AudioWatermarkAttributes forText(String text, TextToSpeechVoice voice, int volume,
                                                   Duration startAt, boolean enabled) {
        if (text == null) {
            throw new NullPointerException("Text-to-speech text is required");
        }
        if (text.trim().isEmpty()) {
            throw new IllegalArgumentException("Text-to-speech text must not be empty");
        }
        Objects.requireNonNull(voice, "Text-to-speech voice is required");
        return new AudioWatermarkAttributes(ContentType.TEXT, null, null, text, voice,
                volume, startAt, enabled);
    }

    public ContentType getContentType() {
        return contentType;
    }

    public Optional<File> getAudioFile() {
        return Optional.ofNullable(audioFile);
    }

    public Optional<byte[]> getAudioBytes() {
        return audioBytes == null
                ? Optional.empty()
                : Optional.of(Arrays.copyOf(audioBytes, audioBytes.length));
    }

    public Optional<String> getText() {
        return Optional.ofNullable(text);
    }

    /** Returns the explicitly selected TTS voice, or empty for the engine default. */
    public Optional<TextToSpeechVoice> getVoice() {
        return Optional.ofNullable(voice);
    }

    /**
     * Returns watermark-layer volume as a percentage. Zero is silent and 100 is unity gain.
     */
    public int getVolume() {
        return volume;
    }

    public Duration getStartAt() {
        return startAt;
    }

    public boolean isEnabled() {
        return enabled;
    }

    private static int requireValidVolume(int volume) {
        if (volume < 0 || volume > 100) {
            throw new IllegalArgumentException("Volume must be between 0 and 100");
        }
        return volume;
    }

    private static Duration requireValidStartAt(Duration startAt) {
        Objects.requireNonNull(startAt, "Start offset is required");
        if (startAt.isNegative()) {
            throw new IllegalArgumentException("Start offset must not be negative");
        }
        return startAt;
    }

    private static File requireReadableFile(File file, String description) {
        Objects.requireNonNull(file, description + " is required");
        if (!file.isFile()) {
            throw new IllegalArgumentException(description + " does not exist or is not a file: " + file);
        }
        if (!file.canRead()) {
            throw new IllegalArgumentException(description + " is not readable: " + file);
        }
        return file;
    }
}
