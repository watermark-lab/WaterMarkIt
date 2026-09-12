package com.markit.audio.ffmpeg;

import java.io.File;
import java.util.Locale;

/** Deterministic output container/codec selection for the initial audio API. */
public enum AudioOutputFormat {
    WAV(".wav", "pcm_s16le"),
    MP3(".mp3", "libmp3lame"),
    FLAC(".flac", "flac"),
    M4A(".m4a", "aac"),
    AAC(".aac", "aac"),
    OGG(".ogg", "libvorbis"),
    OPUS(".opus", "libopus"),
    AIFF(".aiff", "pcm_s16be");

    private final String extension;
    private final String codec;

    AudioOutputFormat(String extension, String codec) {
        this.extension = extension;
        this.codec = codec;
    }

    public String getExtension() {
        return extension;
    }

    public String getCodec() {
        return codec;
    }

    public static AudioOutputFormat forSource(File source) {
        String name = source.getName().toLowerCase(Locale.ROOT);
        if (name.endsWith(".aif")) {
            return AIFF;
        }
        for (AudioOutputFormat format : values()) {
            if (name.endsWith(format.extension)) {
                return format;
            }
        }
        return WAV;
    }
}
