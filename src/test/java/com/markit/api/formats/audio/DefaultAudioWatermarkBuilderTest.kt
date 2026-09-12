package com.markit.api.formats.audio

import com.markit.api.AudioWatermarkAttributes
import com.markit.api.WatermarkService
import com.markit.audio.tts.FreeTtsVoice
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.nio.file.Files
import java.time.Duration
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertIs

class DefaultAudioWatermarkBuilderTest {

    @Test
    fun `root DSL selects audio service for file and bytes`() {
        val file = Files.createTempFile("watermark-audio-source", ".wav").toFile()
        try {
            Files.write(file.toPath(), byteArrayOf(1))
            assertIs<AudioWatermarkContentStep>(WatermarkService.create().watermarkAudio(file))
            assertIs<AudioWatermarkContentStep>(WatermarkService.create().watermarkAudio(byteArrayOf(1)))
        } finally {
            Files.deleteIfExists(file.toPath())
        }
    }

    @Test
    fun `builder captures audio and speech layers with mixing attributes`() {
        lateinit var captured: List<AudioWatermarkAttributes>
        val expected = byteArrayOf(9, 8, 7)
        val builder = DefaultAudioWatermarkBuilder(AudioWatermarkProcessor {
            captured = it
            expected
        })

        val result = builder
            .withAudio(byteArrayOf(1, 2, 3))
            .volume(25)
            .startAt(Duration.ZERO)
            .and()
            .withText("WaterMarkIt")
            .voice(FreeTtsVoice.KEVIN)
            .end()
            .volume(15)
            .startAt(Duration.ofSeconds(30))
            .enableIf(false)
            .apply()

        assertContentEquals(expected, result)
        assertEquals(2, captured.size)
        assertEquals(AudioWatermarkAttributes.ContentType.AUDIO_BYTES, captured[0].contentType)
        assertEquals(25, captured[0].volume)
        assertEquals(Duration.ZERO, captured[0].startAt)
        assertEquals(AudioWatermarkAttributes.ContentType.TEXT, captured[1].contentType)
        assertEquals("WaterMarkIt", captured[1].text.orElseThrow())
        assertEquals(FreeTtsVoice.KEVIN, captured[1].voice.orElseThrow())
        assertEquals(Duration.ofSeconds(30), captured[1].startAt)
        assertEquals(false, captured[1].isEnabled)
    }

    @Test
    fun `builder rejects invalid domain arguments immediately`() {
        val builder = DefaultAudioWatermarkBuilder(AudioWatermarkProcessor { byteArrayOf(1) })

        assertThrows<NullPointerException> { builder.withAudio(null as ByteArray?) }
        assertThrows<IllegalArgumentException> { builder.withAudio(byteArrayOf()) }
        assertThrows<NullPointerException> { builder.withText(null as String?) }
        assertThrows<IllegalArgumentException> { builder.withText("   ") }
        assertThrows<IllegalArgumentException> { builder.withAudio(byteArrayOf(1)).volume(-1) }
        assertThrows<IllegalArgumentException> { builder.volume(101) }
        assertThrows<NullPointerException> { builder.startAt(null) }
        assertThrows<IllegalArgumentException> { builder.startAt(Duration.ofMillis(-1)) }

        val speechBuilder = DefaultAudioWatermarkBuilder(AudioWatermarkProcessor { byteArrayOf(1) })
        assertThrows<NullPointerException> { speechBuilder.withText("hello").voice(null) }

        val fileBuilder = DefaultAudioWatermarkBuilder(AudioWatermarkProcessor { byteArrayOf(1) })
        assertThrows<NullPointerException> { fileBuilder.withAudio(null as java.io.File?) }
        assertThrows<IllegalArgumentException> {
            fileBuilder.withAudio(java.io.File("missing-watermark-audio.wav"))
        }
    }

    @Test
    fun `builder rejects missing watermark content and invalid sources`() {
        assertThrows<NullPointerException> { WatermarkService.create().watermarkAudio(null as ByteArray?) }
        assertThrows<IllegalArgumentException> { WatermarkService.create().watermarkAudio(byteArrayOf()) }
        assertThrows<NullPointerException> { WatermarkService.create().watermarkAudio(null as java.io.File?) }
        assertThrows<IllegalArgumentException> {
            WatermarkService.create().watermarkAudio(java.io.File("missing-audio-file.wav"))
        }
        assertThrows<IllegalArgumentException> {
            DefaultAudioWatermarkBuilder(AudioWatermarkProcessor { byteArrayOf(1) }).apply()
        }
    }
}
