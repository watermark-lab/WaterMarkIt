package com.markit.audio.tts

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class FreeTtsTextToSpeechEngineTest {

    @Test
    fun `offline engine synthesizes WAV bytes`() {
        val audio = FreeTtsTextToSpeechEngine().synthesize("WaterMarkIt")

        assertTrue(audio.size > 44)
        assertEquals("RIFF", audio.copyOfRange(0, 4).toString(Charsets.US_ASCII))
        assertEquals("WAVE", audio.copyOfRange(8, 12).toString(Charsets.US_ASCII))
    }

    @Test
    fun `offline engine rejects a voice from another provider`() {
        val otherProviderVoice = object : TextToSpeechVoice {}
        val error = assertThrows<IllegalArgumentException> {
            FreeTtsTextToSpeechEngine().synthesize("WaterMarkIt", otherProviderVoice)
        }

        assertTrue(error.message.orEmpty().contains("FreeTtsVoice"))
    }

    @Test
    fun `legacy TTS engines reject explicit voice selection by default`() {
        val engine = object : TextToSpeechEngine {
            override fun synthesize(text: String) = byteArrayOf(1)
            override fun getPriority() = 1
        }

        assertThrows<UnsupportedOperationException> {
            engine.synthesize("WaterMarkIt", FreeTtsVoice.KEVIN)
        }
    }
}
