package com.markit.pdf

import com.markit.api.WatermarkingMethod
import com.markit.api.WatermarkService
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.common.PDRectangle
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.IOException
import kotlin.test.assertTrue

class LandscapePageOrientationTextBasedTest : WatermarkPdfTest() {
    @BeforeEach
    override fun initDocument() {
        document = PDDocument().apply {
            val landscapePage = PDPage(PDRectangle.A4).apply {
                mediaBox = PDRectangle(PDRectangle.A4.height, PDRectangle.A4.width)
            }
            addPage(landscapePage)
        }
    }

    @Test
    @Throws(IOException::class)
    fun `given Landscape Pdf when Draw Method then Make Watermarked Pdf`() {
        // When
        val result = WatermarkService.create()
                .watermarkPDF(document)
                    .withText("Sample Watermark").end()
                    .method(WatermarkingMethod.DRAW)
                .apply()

        // Then
        assertTrue(result.isNotEmpty(), "The resulting byte array should not be empty")
        assertTrue(validateImageContent(result));
    }

    @Test
    @Throws(IOException::class)
    fun `given Landscape Pdf when Overlay Method then Make Watermarked Pdf`() {
        // Given
        val watermarkText = "Sample Watermark"

        // When
        val result = WatermarkService.create()
                .watermarkPDF(document)
                    .withText(watermarkText).end()
                    .size(50)
                    .method(WatermarkingMethod.OVERLAY)
                .apply()

        // Then
        assertTrue(result.isNotEmpty(), "The resulting byte array should not be empty")
        assertTrue(validateWatermarkText(result, watermarkText));
    }
}