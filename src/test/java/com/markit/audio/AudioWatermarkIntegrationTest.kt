package com.markit.audio

import com.markit.api.WatermarkService
import com.markit.audio.tts.FreeTtsVoice
import org.junit.jupiter.api.Assumptions.assumeTrue
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.nio.file.Files
import java.nio.file.Paths
import java.time.Duration
import javax.sound.sampled.AudioFileFormat
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.AudioSystem
import kotlin.math.PI
import kotlin.math.sin
import kotlin.test.assertTrue

class AudioWatermarkIntegrationTest {

    @Test
    fun `file audio watermark retains source duration`() {
        assumeTrue(commandExists("ffmpeg"))
        val source = Files.createTempFile("audio-source", ".wav")
        val watermark = Files.createTempFile("audio-watermark", ".wav")
        try {
            Files.write(source, toneWav(1_500, 440.0))
            Files.write(watermark, toneWav(250, 880.0))

            val result = WatermarkService.create()
                .watermarkAudio(source.toFile())
                .withAudio(watermark.toFile())
                .volume(25)
                .startAt(Duration.ofMillis(500))
                .apply()

            assertTrue(result.isNotEmpty())
            assertDurationNear(result, 1_500)
        } finally {
            Files.deleteIfExists(source)
            Files.deleteIfExists(watermark)
        }
    }

    @Test
    fun `byte source mixes audio and TTS watermarks in one result`() {
        assumeTrue(commandExists("ffmpeg"))
        val source = toneWav(1_500, 440.0)
        val chime = toneWav(150, 880.0)

        val result = WatermarkService.create()
            .watermarkAudio(source)
            .withAudio(chime)
            .volume(25)
            .startAt(Duration.ZERO)
            .and()
            .withText("WaterMarkIt")
            .voice(FreeTtsVoice.KEVIN)
            .end()
            .volume(15)
            .startAt(Duration.ofMillis(1_300))
            .apply()

        assertTrue(result.isNotEmpty())
        assertDurationNear(result, 1_500)
    }

    private fun assertDurationNear(wav: ByteArray, expectedMillis: Long) {
        AudioSystem.getAudioInputStream(ByteArrayInputStream(wav)).use { stream ->
            val actualMillis = stream.frameLength * 1_000L / stream.format.frameRate.toLong()
            assertTrue(actualMillis in (expectedMillis - 20)..(expectedMillis + 20))
        }
    }

    private fun toneWav(durationMillis: Int, frequency: Double): ByteArray {
        val sampleRate = 44_100f
        val frameCount = (sampleRate * durationMillis / 1_000).toInt()
        val pcm = ByteArray(frameCount * 2)
        for (frame in 0 until frameCount) {
            val sample = (sin(2 * PI * frequency * frame / sampleRate) * Short.MAX_VALUE * 0.15).toInt()
            pcm[frame * 2] = sample.toByte()
            pcm[frame * 2 + 1] = (sample shr 8).toByte()
        }
        val format = AudioFormat(sampleRate, 16, 1, true, false)
        val output = ByteArrayOutputStream()
        AudioInputStream(ByteArrayInputStream(pcm), format, frameCount.toLong()).use { stream ->
            AudioSystem.write(stream, AudioFileFormat.Type.WAVE, output)
        }
        return output.toByteArray()
    }

    private fun commandExists(command: String): Boolean = try {
        ProcessBuilder(command, "-version").redirectErrorStream(true).start().run {
            inputStream.use { it.readBytes() }
            waitFor() == 0
        }
    } catch (_: Exception) {
        false
    }
}
