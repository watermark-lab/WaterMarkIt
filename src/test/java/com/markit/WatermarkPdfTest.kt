package com.markit

import com.markit.api.WatermarkMethod
import com.markit.api.WatermarkPosition
import com.markit.api.WatermarkService
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.common.PDRectangle
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.awt.Color
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.util.concurrent.Executors
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class WatermarkPdfTest {
    private lateinit var plainDocument: PDDocument
    private lateinit var landscapeDocument: PDDocument

    @BeforeEach
    fun initDocument() {
        plainDocument = PDDocument().apply {
            addPage(PDPage())
        }

        landscapeDocument = PDDocument().apply {
            val landscapePage = PDPage(PDRectangle.A4).apply {
                mediaBox = PDRectangle(PDRectangle.A4.height, PDRectangle.A4.width)
            }
            addPage(landscapePage)
        }
    }

    @Test
    @Throws(IOException::class)
    fun `given Plain Pdf when Draw Method is Used then Make Watermarked Pdf`() {
        // When
        val result = WatermarkService.create(
                Executors.newFixedThreadPool(
                        Runtime.getRuntime().availableProcessors()
                )
        )
                .file(plainDocument)
                .text("Sample Watermark")
                .textSize(50)
                .position(WatermarkPosition.CENTER)
                .method(WatermarkMethod.DRAW)
                .trademark()
                .color(Color.BLACK)
                .dpi(150f)
                .apply()

        // Then
        assertNotNull(result, "The resulting byte array should not be null")
        assertTrue(result.isNotEmpty(), "The resulting byte array should not be empty")
        //outputFile(result, "DrawPlainPdf.pdf")
        plainDocument.close()
    }

    @Test
    @Throws(IOException::class)
    fun `given Plain Pdf when Overlay Method then Make Watermarked Pdf`() {
        // When
        val result = WatermarkService.create()
                .file(plainDocument)
                .text("Sample Watermark")
                .textSize(20)
                .method(WatermarkMethod.OVERLAY) // Overlay mode isn't resource-consuming, so a thread pool isn't necessary.
                .position(WatermarkPosition.TOP_RIGHT)
                .trademark()
                .color(Color.YELLOW)
                .apply()

        // Then
        assertNotNull(result, "The resulting byte array should not be null")
        assertTrue(result.isNotEmpty(), "The resulting byte array should not be empty")
        //outputFile(result, "OverlayPlainPdf.pdf")
        plainDocument.close()
    }

    @Test
    @Throws(IOException::class)
    fun `given Landscape Pdf when Draw Method then Make Watermarked Pdf`() {
        // When
        val result = WatermarkService.create(
                Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())
        )
                .file(landscapeDocument)
                .text("Sample Watermark")
                .position(WatermarkPosition.CENTER)
                .method(WatermarkMethod.DRAW)
                .color(Color.BLUE)
                .dpi(150f)
                .apply()

        // Then
        assertNotNull(result, "The resulting byte array should not be null")
        assertTrue(result.isNotEmpty(), "The resulting byte array should not be empty")
        //outputFile(result, "DrawLandscapePdf.pdf")
        landscapeDocument.close()
    }

    @Test
    @Throws(IOException::class)
    fun `given Landscape Pdf when Overlay Method then Make Watermarked Pdf`() {
        // When
        val result = WatermarkService.create()
                .file(landscapeDocument)
                .text("Sample Watermark")
                .method(WatermarkMethod.OVERLAY) // Overlay mode isn't resource-consuming, so a thread pool isn't necessary.
                .position(WatermarkPosition.BOTTOM_LEFT)
                .trademark()
                .color(Color.GREEN)
                .apply()

        // Then
        assertNotNull(result, "The resulting byte array should not be null")
        assertTrue(result.isNotEmpty(), "The resulting byte array should not be empty")
        //outputFile(result, "OverlayLandscapePdf.pdf")
        landscapeDocument.close()
    }

    private fun outputFile(result: ByteArray, filename: String) {
        val outputFile = File(filename)
        Files.write(outputFile.toPath(), result)
    }
}