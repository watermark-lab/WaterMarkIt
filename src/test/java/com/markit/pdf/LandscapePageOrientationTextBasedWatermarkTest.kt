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

class LandscapePageOrientationTextBasedWatermarkTest : BasePdfWatermarkTest() {
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
                .withText("Sample Watermark")
                    .watermark()
                        .method(WatermarkingMethod.DRAW)
                .apply()

        // Then
        assertTrue(result.isNotEmpty(), "The resulting byte array should not be empty")
        //outputFile(result, "DrawLandscapePdf.pdf")
    }

    @Test
    @Throws(IOException::class)
    fun `given Landscape Pdf when Overlay Method then Make Watermarked Pdf`() {
        // When
        val result = WatermarkService.create()
                .watermarkPDF(document)
                .withText("Sample Watermark")
                    .watermark()
                        .size(50)
                        .method(WatermarkingMethod.OVERLAY)
                .apply()

        // Then
        assertTrue(result.isNotEmpty(), "The resulting byte array should not be empty")
        //outputFile(result, "OverlayLandscapePdf.pdf")
    }
}