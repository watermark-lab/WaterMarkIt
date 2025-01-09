package com.markit

import com.markit.api.WatermarkPosition
import com.markit.api.WatermarkService
import com.markit.api.WatermarkingMethod
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.common.PDRectangle
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File
import java.io.IOException
import java.nio.file.Files
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class PdfTextBasedWatermarkPositionTest {
    private lateinit var document: PDDocument

    @BeforeEach
    fun initDocument() {
        document = PDDocument().apply {
            addPage(PDPage(PDRectangle.A4))
        }
    }

    @AfterEach
    fun close() {
        document.close()
    }

    @Test
    @Throws(IOException::class)
    fun `given Pdf when Center Position and Draw method then Make Watermarked Pdf`() {
        // When
        val result = WatermarkService.textBasedWatermarker()
            .watermark(document)
            .withText("Center Watermark")
            .position(WatermarkPosition.CENTER)
            .method(WatermarkingMethod.DRAW)
            .apply()

        // Then
        assertNotNull(result, "The resulting byte array should not be null")
        assertTrue(result.isNotEmpty(), "The resulting byte array should not be empty")
        //outputFile(result, "TextBasedDrawWatermarkPositionCenter.pdf")
    }

    @Test
    @Throws(IOException::class)
    fun `given Pdf when Top Left Position and Draw method then Make Watermarked Pdf`() {
        // When
        val result = WatermarkService.textBasedWatermarker()
            .watermark(document)
            .withText("Top Left Watermark")
            .position(WatermarkPosition.TOP_LEFT)
            .method(WatermarkingMethod.DRAW)
            .apply()

        // Then
        assertNotNull(result, "The resulting byte array should not be null")
        assertTrue(result.isNotEmpty(), "The resulting byte array should not be empty")
        //outputFile(result, "TextBasedDrawWatermarkPositionTopLeft.pdf")
    }

    @Test
    @Throws(IOException::class)
    fun `given Pdf when Top Right Position and Draw method then Make Watermarked Pdf`() {
        // When
        val result = WatermarkService.textBasedWatermarker()
            .watermark(document)
            .withText("Top Right Watermark")
            .position(WatermarkPosition.TOP_RIGHT)
            .method(WatermarkingMethod.DRAW)
            .apply()

        // Then
        assertNotNull(result, "The resulting byte array should not be null")
        assertTrue(result.isNotEmpty(), "The resulting byte array should not be empty")
        //outputFile(result, "TextBasedDrawWatermarkPositionTopRight.pdf")
    }

    @Test
    @Throws(IOException::class)
    fun `given Pdf when Bottom Left Position and Draw method then Make Watermarked Pdf`() {
        // When
        val result = WatermarkService.textBasedWatermarker()
            .watermark(document)
            .withText("Bottom Left Watermark")
            .position(WatermarkPosition.BOTTOM_LEFT)
            .method(WatermarkingMethod.DRAW)
            .apply()

        // Then
        assertNotNull(result, "The resulting byte array should not be null")
        assertTrue(result.isNotEmpty(), "The resulting byte array should not be empty")
        //outputFile(result, "TextBasedDrawWatermarkPositionBottomLeft.pdf")
    }

    @Test
    @Throws(IOException::class)
    fun `given Pdf when Bottom Right Position and Draw method then Make Watermarked Pdf`() {
        // When
        val result = WatermarkService.textBasedWatermarker()
            .watermark(document)
            .withText("Bottom Right Watermark")
            .position(WatermarkPosition.BOTTOM_RIGHT)
            .method(WatermarkingMethod.DRAW)
            .apply()

        // Then
        assertNotNull(result, "The resulting byte array should not be null")
        assertTrue(result.isNotEmpty(), "The resulting byte array should not be empty")
        //outputFile(result, "TextBasedDrawWatermarkPositionBottomRight.pdf")
    }

    @Test
    @Throws(IOException::class)
    fun `given Pdf when Tiled Position and Draw method then Make Watermarked Pdf`() {
        // When
        val result = WatermarkService.textBasedWatermarker()
            .watermark(document)
            .withText("Tiled Watermark")
            .position(WatermarkPosition.TILED)
            .method(WatermarkingMethod.DRAW)
            .apply()

        // Then
        assertNotNull(result, "The resulting byte array should not be null")
        assertTrue(result.isNotEmpty(), "The resulting byte array should not be empty")
        //outputFile(result, "TextBasedDrawWatermarkPositionTiled.pdf")
    }

    @Test
    @Throws(IOException::class)
    fun `given Pdf when Center Position and Overlay method then Make Watermarked Pdf`() {
        // When
        val result = WatermarkService.textBasedWatermarker()
            .watermark(document)
            .withText("Center Watermark").size(25)
            .position(WatermarkPosition.CENTER)
            .method(WatermarkingMethod.OVERLAY)
            .apply()

        // Then
        assertNotNull(result, "The resulting byte array should not be null")
        assertTrue(result.isNotEmpty(), "The resulting byte array should not be empty")
        //outputFile(result, "TextBasedOverlayWatermarkPositionCenter.pdf")
    }

    @Test
    @Throws(IOException::class)
    fun `given Pdf when Top Left Position and Overlay method then Make Watermarked Pdf`() {
        // When
        val result = WatermarkService.textBasedWatermarker()
            .watermark(document)
            .withText("Top Left Watermark").size(25)
            .position(WatermarkPosition.TOP_LEFT)
            .method(WatermarkingMethod.OVERLAY)
            .apply()

        // Then
        assertNotNull(result, "The resulting byte array should not be null")
        assertTrue(result.isNotEmpty(), "The resulting byte array should not be empty")
        //outputFile(result, "TextBasedOverlayWatermarkPositionTopLeft.pdf")
    }

    @Test
    @Throws(IOException::class)
    fun `given Pdf when Top Right Position and Overlay method then Make Watermarked Pdf`() {
        // When
        val result = WatermarkService.textBasedWatermarker()
            .watermark(document)
            .withText("Top Right Watermark").size(25)
            .position(WatermarkPosition.TOP_RIGHT)
            .method(WatermarkingMethod.OVERLAY)
            .apply()

        // Then
        assertNotNull(result, "The resulting byte array should not be null")
        assertTrue(result.isNotEmpty(), "The resulting byte array should not be empty")
        //outputFile(result, "TextBasedOverlayWatermarkPositionTopRight.pdf")
    }

    @Test
    @Throws(IOException::class)
    fun `given Pdf when Bottom Left Position and Overlay method then Make Watermarked Pdf`() {
        // When
        val result = WatermarkService.textBasedWatermarker()
            .watermark(document)
            .withText("Bottom Left Watermark").size(25)
            .position(WatermarkPosition.BOTTOM_LEFT)
            .method(WatermarkingMethod.OVERLAY)
            .apply()

        // Then
        assertNotNull(result, "The resulting byte array should not be null")
        assertTrue(result.isNotEmpty(), "The resulting byte array should not be empty")
        //outputFile(result, "TextBasedOverlayWatermarkPositionBottomLeft.pdf")
    }

    @Test
    @Throws(IOException::class)
    fun `given Pdf when Bottom Right Position and Overlay method then Make Watermarked Pdf`() {
        // When
        val result = WatermarkService.textBasedWatermarker()
            .watermark(document)
            .withText("Bottom Right Watermark").size(25)
            .position(WatermarkPosition.BOTTOM_RIGHT)
            .method(WatermarkingMethod.OVERLAY)
            .apply()

        // Then
        assertNotNull(result, "The resulting byte array should not be null")
        assertTrue(result.isNotEmpty(), "The resulting byte array should not be empty")
        //outputFile(result, "TextBasedOverlayWatermarkPositionBottomRight.pdf")
    }

    private fun outputFile(result: ByteArray, filename: String) {
        val outputFile = File(filename)
        Files.write(outputFile.toPath(), result)
    }

}

