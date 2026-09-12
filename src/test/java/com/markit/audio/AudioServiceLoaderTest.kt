package com.markit.audio

import com.markit.audio.ffmpeg.FFmpegAudioWatermarker
import com.markit.audio.tts.FreeTtsTextToSpeechEngine
import com.markit.audio.tts.TextToSpeechEngine
import com.markit.servicelocator.DefaultServiceLocator
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

class AudioServiceLoaderTest {

    @Test
    fun `service loader discovers default audio watermarker`() {
        assertTrue(DefaultServiceLocator.find(AudioWatermarker::class.java).any {
            it is FFmpegAudioWatermarker
        })
    }

    @Test
    fun `service loader discovers default local TTS engine`() {
        assertTrue(DefaultServiceLocator.find(TextToSpeechEngine::class.java).any {
            it is FreeTtsTextToSpeechEngine
        })
    }
}
