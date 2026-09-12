package com.markit.audio.ffmpeg

import com.markit.api.AudioWatermarkAttributes
import org.junit.jupiter.api.Assumptions.assumeTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.time.Duration
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DefaultAudioFilterChainBuilderTest {

    @Test
    fun `filter graph maps every input and preserves source gain and duration`() {
        val filter = DefaultAudioFilterChainBuilder().build(
            listOf(
                AudioWatermarkAttributes.forAudio(byteArrayOf(1), 25, Duration.ZERO, true),
                AudioWatermarkAttributes.forText("WaterMarkIt", 15, Duration.ofSeconds(3), true)
            )
        )

        assertTrue(filter.filter.contains("[0:a:0]"))
        assertTrue(filter.filter.contains("[1:a:0]"))
        assertTrue(filter.filter.contains("volume=0.2500,adelay=0:all=1[wm0]"))
        assertTrue(filter.filter.contains("[2:a:0]"))
        assertTrue(filter.filter.contains("volume=0.1500,adelay=3000:all=1[wm1]"))
        assertTrue(filter.filter.contains("[source][wm0][wm1]amix=inputs=3:duration=first"))
        assertTrue(filter.filter.contains("normalize=0[aout]"))
        assertEquals("[aout]", filter.outputLabel)
    }

    @Test
    fun `command uses argument list with correct mappings`() {
        val filter = AudioFilterResult("[0:a]anull[aout]", "[aout]")
        val command = FFmpegAudioCommandExecutor().buildCommand(
            File("source with spaces.wav"),
            listOf(File("watermark with spaces.wav")),
            filter,
            AudioOutputFormat.WAV,
            Path.of("result.wav")
        )

        assertEquals("ffmpeg", command.first())
        assertEquals("source with spaces.wav", File(command[command.indexOf("-i") + 1]).name)
        assertTrue(command.contains("[0:a]anull[aout]"))
        assertTrue(command.contains("[aout]"))
        assertEquals("pcm_s16le", command[command.indexOf("-c:a") + 1])
        assertEquals("result.wav", Path.of(command.last()).fileName.toString())
    }

    @Test
    fun `ffmpeg failure reports exit code and diagnostic tail`() {
        assumeTrue(commandExists("ffmpeg"))
        val invalidSource = Files.createTempFile("invalid-audio", ".wav")
        try {
            Files.writeString(invalidSource, "not audio")
            val error = assertThrows<IOException> {
                FFmpegAudioCommandExecutor().execute(
                    invalidSource.toFile(),
                    listOf(invalidSource.toFile()),
                    AudioFilterResult("[0:a][1:a]amix=inputs=2[aout]", "[aout]"),
                    AudioOutputFormat.WAV
                )
            }
            assertTrue(error.message.orEmpty().contains("exit code"))
            assertTrue(error.message.orEmpty().contains("Diagnostic tail"))
        } finally {
            Files.deleteIfExists(invalidSource)
        }
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
