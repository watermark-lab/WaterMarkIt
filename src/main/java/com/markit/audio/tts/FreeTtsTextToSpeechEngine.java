package com.markit.audio.tts;

import com.markit.audio.ffmpeg.TemporaryFiles;
import com.markit.servicelocator.Prioritizable;
import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
import com.sun.speech.freetts.audio.SingleFileAudioPlayer;

import javax.sound.sampled.AudioFileFormat;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

/** Offline US-English speech synthesis backed by the FreeTTS Kevin voice. */
public class FreeTtsTextToSpeechEngine implements TextToSpeechEngine {

    @Override
    public synchronized byte[] synthesize(String text) throws Exception {
        return synthesize(text, FreeTtsVoice.KEVIN_16);
    }

    @Override
    public synchronized byte[] synthesize(String text, TextToSpeechVoice selectedVoice) throws Exception {
        if (text == null) {
            throw new NullPointerException("Text-to-speech text is required");
        }
        if (text.trim().isEmpty()) {
            throw new IllegalArgumentException("Text-to-speech text must not be empty");
        }
        Objects.requireNonNull(selectedVoice, "Text-to-speech voice is required");
        if (!(selectedVoice instanceof FreeTtsVoice)) {
            throw new IllegalArgumentException("FreeTTS requires a FreeTtsVoice value");
        }
        String voiceName = ((FreeTtsVoice) selectedVoice).getFreeTtsName();
        Voice voice = VoiceManager.getInstance().getVoice(voiceName);
        if (voice == null) {
            throw new IllegalArgumentException("FreeTTS voice '" + voiceName
                    + "' is not available. Bundled voices: kevin, kevin16");
        }

        try (TemporaryFiles temporaryFiles = new TemporaryFiles("wmk-tts-")) {
            Path output = temporaryFiles.resolve("speech.wav");
            String baseName = output.toString().substring(0, output.toString().length() - 4);
            SingleFileAudioPlayer player = new SingleFileAudioPlayer(baseName, AudioFileFormat.Type.WAVE);

            try {
                voice.setAudioPlayer(player);
                voice.allocate();
                if (!voice.speak(text)) {
                    throw new IllegalStateException("FreeTTS did not synthesize the supplied text");
                }
            } finally {
                try {
                    player.close();
                } finally {
                    voice.deallocate();
                }
            }
            return Files.readAllBytes(output);
        }
    }

    @Override
    public int getPriority() {
        return Prioritizable.DEFAULT_PRIORITY;
    }
}
