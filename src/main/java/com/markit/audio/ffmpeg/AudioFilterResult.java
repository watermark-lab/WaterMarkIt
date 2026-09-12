package com.markit.audio.ffmpeg;

import java.util.Objects;

/** Immutable FFmpeg audio filter graph and its mapped output label. */
public final class AudioFilterResult {

    private final String filter;
    private final String outputLabel;

    public AudioFilterResult(String filter, String outputLabel) {
        this.filter = Objects.requireNonNull(filter, "Filter graph is required");
        this.outputLabel = Objects.requireNonNull(outputLabel, "Output label is required");
    }

    public String getFilter() {
        return filter;
    }

    public String getOutputLabel() {
        return outputLabel;
    }
}
