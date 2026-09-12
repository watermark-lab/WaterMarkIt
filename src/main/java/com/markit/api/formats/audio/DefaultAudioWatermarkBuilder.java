package com.markit.api.formats.audio;

import com.markit.api.AudioWatermarkAttributes;
import com.markit.audio.AudioWatermarker;
import com.markit.audio.tts.TextToSpeechVoice;
import com.markit.exceptions.WatermarkingException;
import com.markit.servicelocator.ServiceFactory;

import java.io.File;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** Default implementation of the audio watermark DSL. */
public final class DefaultAudioWatermarkBuilder implements AudioWatermarkContentStep,
        WatermarkAudioBuilder,
        TextToSpeechWatermarkBuilder {

    private static final int DEFAULT_VOLUME = 100;

    private final AudioWatermarkProcessor processor;
    private final List<AudioWatermarkAttributes> watermarks = new ArrayList<>();
    private Draft current = new Draft();

    public DefaultAudioWatermarkBuilder(byte[] sourceAudio) {
        byte[] sourceCopy = validateSourceBytes(sourceAudio);
        this.processor = watermarks -> getAudioWatermarker().watermark(sourceCopy, watermarks);
    }

    public DefaultAudioWatermarkBuilder(File sourceAudio) {
        File validatedSource = validateSourceFile(sourceAudio);
        this.processor = watermarks -> getAudioWatermarker().watermark(validatedSource, watermarks);
    }

    DefaultAudioWatermarkBuilder(AudioWatermarkProcessor processor) {
        this.processor = Objects.requireNonNull(processor, "Audio watermark processor is required");
    }

    @Override
    public WatermarkAudioBuilder withAudio(File watermark) {
        ensureContentNotSelected();
        current.file = validateReadableFile(watermark, "Watermark audio file");
        return this;
    }

    @Override
    public WatermarkAudioBuilder withAudio(byte[] watermark) {
        ensureContentNotSelected();
        if (watermark == null) {
            throw new NullPointerException("Watermark audio bytes are required");
        }
        if (watermark.length == 0) {
            throw new IllegalArgumentException("Watermark audio bytes must not be empty");
        }
        current.bytes = watermark.clone();
        return this;
    }

    @Override
    public TextToSpeechWatermarkBuilder withText(String text) {
        ensureContentNotSelected();
        if (text == null) {
            throw new NullPointerException("Text-to-speech text is required");
        }
        if (text.trim().isEmpty()) {
            throw new IllegalArgumentException("Text-to-speech text must not be empty");
        }
        current.text = text;
        return this;
    }

    @Override
    public TextToSpeechWatermarkBuilder voice(TextToSpeechVoice voice) {
        if (current.text == null) {
            throw new IllegalStateException("Select text before choosing a text-to-speech voice");
        }
        current.voice = Objects.requireNonNull(voice, "Text-to-speech voice is required");
        return this;
    }

    @Override
    public WatermarkAudioBuilder end() {
        return this;
    }

    @Override
    public WatermarkAudioBuilder volume(int volume) {
        if (volume < 0 || volume > 100) {
            throw new IllegalArgumentException("Volume must be between 0 and 100");
        }
        current.volume = volume;
        return this;
    }

    @Override
    public WatermarkAudioBuilder startAt(Duration offset) {
        Objects.requireNonNull(offset, "Start offset is required");
        if (offset.isNegative()) {
            throw new IllegalArgumentException("Start offset must not be negative");
        }
        current.startAt = offset;
        return this;
    }

    @Override
    public WatermarkAudioBuilder enableIf(boolean condition) {
        current.enabled = condition;
        return this;
    }

    @Override
    public AudioWatermarkContentStep and() {
        approveCurrent();
        current = new Draft();
        return this;
    }

    @Override
    public byte[] apply() {
        approveCurrent();
        try {
            return processor.apply(new ArrayList<>(watermarks));
        } catch (WatermarkingException e) {
            throw e;
        } catch (Exception e) {
            throw new WatermarkingException("Error watermarking the audio", e);
        }
    }

    private void approveCurrent() {
        watermarks.add(current.toAttributes());
    }

    private void ensureContentNotSelected() {
        if (current.hasContent()) {
            throw new IllegalStateException("Watermark content has already been selected; use and() for another layer");
        }
    }

    private static byte[] validateSourceBytes(byte[] sourceAudio) {
        if (sourceAudio == null) {
            throw new NullPointerException("Source audio bytes are required");
        }
        if (sourceAudio.length == 0) {
            throw new IllegalArgumentException("Source audio bytes must not be empty");
        }
        return sourceAudio.clone();
    }

    private static File validateSourceFile(File sourceAudio) {
        return validateReadableFile(sourceAudio, "Source audio file");
    }

    private static File validateReadableFile(File file, String description) {
        Objects.requireNonNull(file, description + " is required");
        if (!file.isFile()) {
            throw new IllegalArgumentException(description + " does not exist or is not a file: " + file);
        }
        if (!file.canRead()) {
            throw new IllegalArgumentException(description + " is not readable: " + file);
        }
        return file;
    }

    private static AudioWatermarker getAudioWatermarker() {
        return (AudioWatermarker) ServiceFactory.getInstance().getService(AudioWatermarker.class);
    }

    private static final class Draft {
        private File file;
        private byte[] bytes;
        private String text;
        private TextToSpeechVoice voice;
        private int volume = DEFAULT_VOLUME;
        private Duration startAt = Duration.ZERO;
        private boolean enabled = true;

        private boolean hasContent() {
            return file != null || bytes != null || text != null;
        }

        private AudioWatermarkAttributes toAttributes() {
            if (file != null) {
                return AudioWatermarkAttributes.forAudio(file, volume, startAt, enabled);
            }
            if (bytes != null) {
                return AudioWatermarkAttributes.forAudio(bytes, volume, startAt, enabled);
            }
            if (text != null) {
                return voice == null
                        ? AudioWatermarkAttributes.forText(text, volume, startAt, enabled)
                        : AudioWatermarkAttributes.forText(text, voice, volume, startAt, enabled);
            }
            throw new IllegalArgumentException("Configure watermark audio or text before and() or apply()");
        }
    }
}
