package com.markit.audio.ffmpeg;

import com.markit.api.AudioWatermarkAttributes;
import com.markit.servicelocator.Prioritizable;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

/** Default single-pass audio mixing graph builder. */
public class DefaultAudioFilterChainBuilder implements AudioFilterChainBuilder {

    private static final String NORMALIZE =
            "aresample=44100,aformat=sample_fmts=fltp:channel_layouts=stereo";

    @Override
    public AudioFilterResult build(List<AudioWatermarkAttributes> watermarks) {
        Objects.requireNonNull(watermarks, "Watermarks are required");
        if (watermarks.isEmpty()) {
            throw new IllegalArgumentException("At least one enabled watermark is required");
        }

        StringBuilder graph = new StringBuilder("[0:a:0]")
                .append(NORMALIZE)
                .append("[source]");
        StringBuilder mixInputs = new StringBuilder("[source]");

        for (int i = 0; i < watermarks.size(); i++) {
            AudioWatermarkAttributes watermark = Objects.requireNonNull(
                    watermarks.get(i), "Watermark attributes must not contain null");
            if (!watermark.isEnabled()) {
                throw new IllegalArgumentException("Filter graph accepts enabled watermarks only");
            }
            long delayMillis;
            try {
                delayMillis = watermark.getStartAt().toMillis();
            } catch (ArithmeticException e) {
                throw new IllegalArgumentException("Start offset is too large for FFmpeg milliseconds", e);
            }
            String label = "wm" + i;
            graph.append(';')
                    .append('[').append(i + 1).append(":a:0]")
                    .append(NORMALIZE)
                    .append(",volume=")
                    .append(String.format(Locale.ROOT, "%.4f", watermark.getVolume() / 100.0d))
                    .append(",adelay=").append(delayMillis).append(":all=1")
                    .append('[').append(label).append(']');
            mixInputs.append('[').append(label).append(']');
        }

        graph.append(';').append(mixInputs)
                .append("amix=inputs=").append(watermarks.size() + 1)
                .append(":duration=first:dropout_transition=0:normalize=0[aout]");
        return new AudioFilterResult(graph.toString(), "[aout]");
    }

    @Override
    public int getPriority() {
        return Prioritizable.DEFAULT_PRIORITY;
    }
}
