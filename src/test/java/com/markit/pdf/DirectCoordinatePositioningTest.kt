package com.markit.pdf

import com.markit.api.WatermarkService
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.awt.Color
import java.io.IOException
import java.util.concurrent.Executors
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class DirectCoordinatePositioningTest : BasePdfWatermarkTest() {
    @BeforeEach
    override fun initDocument() {
        document = PDDocument().apply {
            addPage(PDPage())
        }
    }

    @Test
    @Throws(IOException::class)
    fun `given Pdf when Apply Watermark with Direct Coordinates then Position Correctly`() {
        // When
        val result = WatermarkService.create(
                Executors.newFixedThreadPool(
                        Runtime.getRuntime().availableProcessors()
                )
        )
            .watermarkPDF(document)
                .withText("Direct Coordinate Test")
                    .color(Color.RED).end()
                .position(500, 600) // Direct coordinates
                .opacity(50)
                .size(30)
            .apply()

        // Then
        assertNotNull(result, "The resulting byte array should not be null")
        assertTrue(result.isNotEmpty(), "The resulting byte array should not be empty")
    }


    @Test
    @Throws(IOException::class)
    fun `given Pdf when Apply Image Watermark with Direct Coordinates then Position Correctly`() {
        // When
        val result = WatermarkService.create(
                Executors.newFixedThreadPool(
                        Runtime.getRuntime().availableProcessors()
                )
        )
            .watermarkPDF(document)
                .withImage(readFileFromClasspathAsBytes("logo.png"))
                .position(150, 250) // Direct coordinates for image
                .opacity(30)
                .size(80)
            .apply()

        // Then
        assertNotNull(result, "The resulting byte array should not be null")
        assertTrue(result.isNotEmpty(), "The resulting byte array should not be empty")
    }
}
