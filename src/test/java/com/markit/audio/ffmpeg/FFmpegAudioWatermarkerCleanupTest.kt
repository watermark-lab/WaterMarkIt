package com.markit.audio.ffmpeg

import com.markit.api.AudioWatermarkAttributes
import com.markit.audio.tts.TextToSpeechEngine
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.File
import java.time.Duration
import kotlin.test.assertContentEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class FFmpegAudioWatermarkerCleanupTest {

    @Test
    fun `temporary source watermark and speech files are cleaned after success`() {
        val executor = RecordingExecutor(false)
        val watermarker = FFmpegAudioWatermarker(
            DefaultAudioFilterChainBuilder(), executor, StubTts()
        )
        val result = watermarker.watermark(
            byteArrayOf(1, 2),
            listOf(
                AudioWatermarkAttributes.forAudio(byteArrayOf(3), 100, Duration.ZERO, true),
                AudioWatermarkAttributes.forText("hello", 20, Duration.ZERO, true)
            )
        )

        assertContentEquals(byteArrayOf(7), result)
        assertTrue(executor.existedDuringExecution)
        assertTrue(executor.paths.all { !it.exists() })
    }

    @Test
    fun `temporary files are cleaned after processing failure`() {
        val executor = RecordingExecutor(true)
        val watermarker = FFmpegAudioWatermarker(
            DefaultAudioFilterChainBuilder(), executor, StubTts()
        )

        assertThrows<IllegalStateException> {
            watermarker.watermark(
                byteArrayOf(1, 2),
                listOf(AudioWatermarkAttributes.forAudio(byteArrayOf(3), 100, Duration.ZERO, true))
            )
        }
        assertTrue(executor.existedDuringExecution)
        assertTrue(executor.paths.all { !it.exists() })
    }

    @Test
    fun `disabled watermark does not invoke processing`() {
        val executor = RecordingExecutor(true)
        val watermarker = FFmpegAudioWatermarker(
            DefaultAudioFilterChainBuilder(), executor, StubTts()
        )
        val source = byteArrayOf(1, 2)

        val result = watermarker.watermark(
            source,
            listOf(AudioWatermarkAttributes.forAudio(byteArrayOf(3), 100, Duration.ZERO, false))
        )

        assertContentEquals(source, result)
        assertFalse(executor.invoked)
    }

    private class RecordingExecutor(private val fail: Boolean) : AudioCommandExecutor {
        val paths = mutableListOf<File>()
        var existedDuringExecution = false
        var invoked = false

        override fun execute(
            source: File,
            watermarkInputs: List<File>,
            filter: AudioFilterResult,
            outputFormat: AudioOutputFormat
        ): ByteArray {
            invoked = true
            paths.add(source)
            paths.addAll(watermarkInputs)
            existedDuringExecution = paths.all { it.exists() }
            if (fail) throw IllegalStateException("simulated ffmpeg failure")
            return byteArrayOf(7)
        }

        override fun getPriority() = 1
    }

    private class StubTts : TextToSpeechEngine {
        override fun synthesize(text: String) = byteArrayOf(4)
        override fun getPriority() = 1
    }
}
