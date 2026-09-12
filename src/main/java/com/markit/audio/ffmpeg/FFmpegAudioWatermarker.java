package com.markit.audio.ffmpeg;

import com.markit.api.AudioWatermarkAttributes;
import com.markit.audio.AudioWatermarker;
import com.markit.audio.tts.TextToSpeechEngine;
import com.markit.servicelocator.Prioritizable;
import com.markit.servicelocator.ServiceFactory;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/** Default audible-watermark engine using one FFmpeg mixing invocation. */
public class FFmpegAudioWatermarker implements AudioWatermarker {

    private final AudioFilterChainBuilder filterBuilder;
    private final AudioCommandExecutor commandExecutor;
    private final TextToSpeechEngine textToSpeechEngine;

    public FFmpegAudioWatermarker() {
        this(getService(AudioFilterChainBuilder.class),
                getService(AudioCommandExecutor.class),
                getService(TextToSpeechEngine.class));
    }

    FFmpegAudioWatermarker(AudioFilterChainBuilder filterBuilder, AudioCommandExecutor commandExecutor,
                           TextToSpeechEngine textToSpeechEngine) {
        this.filterBuilder = Objects.requireNonNull(filterBuilder, "Audio filter builder is required");
        this.commandExecutor = Objects.requireNonNull(commandExecutor, "Audio command executor is required");
        this.textToSpeechEngine = Objects.requireNonNull(textToSpeechEngine, "Text-to-speech engine is required");
    }

    @Override
    public byte[] watermark(byte[] sourceAudio, List<AudioWatermarkAttributes> watermarks) throws Exception {
        if (sourceAudio == null) {
            throw new NullPointerException("Source audio bytes are required");
        }
        if (sourceAudio.length == 0) {
            throw new IllegalArgumentException("Source audio bytes must not be empty");
        }
        List<AudioWatermarkAttributes> enabled = enabledWatermarks(watermarks);
        if (enabled.isEmpty()) {
            return sourceAudio.clone();
        }

        try (TemporaryFiles temporaryFiles = new TemporaryFiles("wmk-audio-input-")) {
            File source = temporaryFiles.write("source.audio", sourceAudio).toFile();
            return watermark(source, enabled, AudioOutputFormat.WAV, temporaryFiles);
        }
    }

    @Override
    public byte[] watermark(File sourceAudio, List<AudioWatermarkAttributes> watermarks) throws Exception {
        validateReadableSource(sourceAudio);
        List<AudioWatermarkAttributes> enabled = enabledWatermarks(watermarks);
        if (enabled.isEmpty()) {
            return Files.readAllBytes(sourceAudio.toPath());
        }
        try (TemporaryFiles temporaryFiles = new TemporaryFiles("wmk-audio-input-")) {
            return watermark(sourceAudio, enabled, AudioOutputFormat.forSource(sourceAudio), temporaryFiles);
        }
    }

    private byte[] watermark(File source, List<AudioWatermarkAttributes> enabled,
                             AudioOutputFormat outputFormat, TemporaryFiles temporaryFiles) throws Exception {
        List<File> inputs = new ArrayList<>();
        for (int i = 0; i < enabled.size(); i++) {
            inputs.add(prepareInput(enabled.get(i), i, temporaryFiles));
        }
        AudioFilterResult filter = filterBuilder.build(enabled);
        return commandExecutor.execute(source, Collections.unmodifiableList(inputs), filter, outputFormat);
    }

    private File prepareInput(AudioWatermarkAttributes watermark, int index,
                              TemporaryFiles temporaryFiles) throws Exception {
        switch (watermark.getContentType()) {
            case AUDIO_FILE:
                return watermark.getAudioFile().orElseThrow(
                        () -> new IllegalArgumentException("Audio-file watermark has no file"));
            case AUDIO_BYTES:
                byte[] bytes = watermark.getAudioBytes().orElseThrow(
                        () -> new IllegalArgumentException("Audio-bytes watermark has no bytes"));
                return temporaryFiles.write("watermark-" + index + ".audio", bytes).toFile();
            case TEXT:
                String text = watermark.getText().orElseThrow(
                        () -> new IllegalArgumentException("Text watermark has no text"));
                byte[] speech = watermark.getVoice().isPresent()
                        ? textToSpeechEngine.synthesize(text, watermark.getVoice().get())
                        : textToSpeechEngine.synthesize(text);
                if (speech == null || speech.length == 0) {
                    throw new IllegalStateException("Text-to-speech engine returned empty audio");
                }
                return temporaryFiles.write("speech-" + index + ".wav", speech).toFile();
            default:
                throw new IllegalArgumentException("Unsupported audio watermark content: "
                        + watermark.getContentType());
        }
    }

    private List<AudioWatermarkAttributes> enabledWatermarks(List<AudioWatermarkAttributes> watermarks) {
        Objects.requireNonNull(watermarks, "Watermarks are required");
        if (watermarks.stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("Watermarks must not contain null");
        }
        return watermarks.stream().filter(AudioWatermarkAttributes::isEnabled).collect(Collectors.toList());
    }

    private void validateReadableSource(File sourceAudio) {
        Objects.requireNonNull(sourceAudio, "Source audio file is required");
        if (!sourceAudio.isFile()) {
            throw new IllegalArgumentException("Source audio file does not exist or is not a file: " + sourceAudio);
        }
        if (!sourceAudio.canRead()) {
            throw new IllegalArgumentException("Source audio file is not readable: " + sourceAudio);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T extends Prioritizable> T getService(Class<T> type) {
        return (T) ServiceFactory.getInstance().getService(type);
    }

    @Override
    public int getPriority() {
        return Prioritizable.DEFAULT_PRIORITY;
    }
}
