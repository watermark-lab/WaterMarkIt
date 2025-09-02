package com.markit.pdf

import com.markit.utils.FileUtils
import com.markit.api.positioning.WatermarkPosition
import com.markit.api.WatermarkService
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.common.PDRectangle
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.IOException
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class ImageBasedWatermarkTest : WatermarkPdfTest() {
    @BeforeEach
    override fun initDocument() {
        document = PDDocument().apply {
            addPage(PDPage(PDRectangle.A4))
        }
    }

    @Test
    @Throws(IOException::class)
    fun `given Pdf when Image Watermark and DPI then Apply Watermark`() {
        // When
        val result = WatermarkService.create()
            .watermarkPDF(document)
                .withImage(FileUtils.readFileFromClasspathAsBytes("logo.png"))
                    .size(15)
                    .dpi(100)
                    .position(WatermarkPosition.CENTER).end()
            .apply()

        // Then
        assertNotNull(result, "The resulting byte array should not be null")
        assertTrue(result.isNotEmpty(), "The resulting byte array should not be empty")
        assertTrue(validateImageContent(result));
    }

    @Test
    @Throws(IOException::class)
    fun `given Pdf when Image Watermark and Adjust then Apply Watermark`() {
        // When
        val result = WatermarkService.create()
            .watermarkPDF(document)
                .withImage(FileUtils.readFileFromClasspathAsBytes("logo.png"))
                    .size(25)
                    .position(WatermarkPosition.TILED)
                        .adjust(50, 50)
                        .end()
            .apply()

        // Then
        assertNotNull(result, "The resulting byte array should not be null")
        assertTrue(result.isNotEmpty(), "The resulting byte array should not be empty")
        assertTrue(validateImageContent(result));
    }

    @Test
    @Throws(IOException::class)
    fun `given Pdf when Image Watermark with TOP_CENTER position then apply Watermark`() {
        // When
        val result = WatermarkService.create()
            .watermarkPDF(document)
                .withImage(FileUtils.readFileFromClasspathAsBytes("logo.png"))
                    .size(25)
                    .position(WatermarkPosition.TOP_CENTER)
                    .end()
            .apply()

        // Then
        assertNotNull(result, "The resulting byte array should not be null")
        assertTrue(result.isNotEmpty(), "The resulting byte array should not be empty")
        assertTrue(validateImageContent(result));
    }

    @Test
    @Throws(IOException::class)
    fun `given Pdf when Image Watermark with BOTTOM_CENTER position then Apply Watermark`() {
        // When
        val result = WatermarkService.create()
            .watermarkPDF(document)
                .withImage(FileUtils.readFileFromClasspathAsBytes("logo.png"))
                    .size(25)
                    .position(WatermarkPosition.BOTTOM_CENTER)
                    .end()
            .apply()

        // Then
        assertNotNull(result, "The resulting byte array should not be null")
        assertTrue(result.isNotEmpty(), "The resulting byte array should not be empty")
        assertTrue(validateImageContent(result));
    }
}