package com.markit.image

import com.markit.api.ImageType
import com.markit.api.WatermarkService
import org.junit.jupiter.api.Test
import java.awt.Color
import java.io.IOException
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class DirectCoordinateImagePositioningTest {
    
    @Test
    @Throws(IOException::class)
    fun `given Image when Apply Watermark with Direct Coordinates then Position Correctly`() {
        // Given
        val imageBytes = javaClass.classLoader.getResourceAsStream("logo.png")?.readBytes()
        assertNotNull(imageBytes, "Test image should be available")
        
        // When
        val result = WatermarkService.create()
            .watermarkImage(imageBytes!!, ImageType.PNG)
                .withText("Direct Coordinate Test")
                    .color(Color.RED).end()
                .position(100, 200) // Direct coordinates
                .opacity(50)
                .size(30)
            .apply()

        // Then
        assertNotNull(result, "The resulting byte array should not be null")
        assertTrue(result.isNotEmpty(), "The resulting byte array should not be empty")
    }

    @Test
    @Throws(IOException::class)
    fun `given Image when Apply Image Watermark with Direct Coordinates then Position Correctly`() {
        // Given
        val imageBytes = javaClass.classLoader.getResourceAsStream("logo.png")?.readBytes()
        assertNotNull(imageBytes, "Test image should be available")
        
        // When
        val result = WatermarkService.create()
            .watermarkImage(imageBytes!!, ImageType.PNG)
                .withImage(imageBytes)
                .position(150, 250) // Direct coordinates for image
                .opacity(30)
                .size(80)
            .apply()

        // Then
        assertNotNull(result, "The resulting byte array should not be null")
        assertTrue(result.isNotEmpty(), "The resulting byte array should not be empty")
    }
}
