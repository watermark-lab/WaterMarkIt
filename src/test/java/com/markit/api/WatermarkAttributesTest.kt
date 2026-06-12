package com.markit.api

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class WatermarkAttributesTest {

    @Test
    fun `given opacity within range when opacityFraction then normalize to 0_0-1_0`() {
        assertEquals(0.0f, WatermarkAttributes(opacity = 0).opacityFraction)
        assertEquals(0.4f, WatermarkAttributes(opacity = 40).opacityFraction)
        assertEquals(1.0f, WatermarkAttributes(opacity = 100).opacityFraction)
    }

    @Test
    fun `given opacity out of range when opacityFraction then clamp to bounds`() {
        assertEquals(0.0f, WatermarkAttributes(opacity = -20).opacityFraction)
        assertEquals(1.0f, WatermarkAttributes(opacity = 150).opacityFraction)
    }
}
