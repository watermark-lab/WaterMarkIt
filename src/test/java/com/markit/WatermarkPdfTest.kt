package com.markit

import com.markit.api.WatermarkMethod
import com.markit.api.WatermarkPosition
import com.markit.api.WatermarkService
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.common.PDRectangle
import org.junit.jupiter.api.AfterEach
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

    @AfterEach
    fun close(){
        plainDocument.close()
        landscapeDocument.close()
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
            .watermark(plainDocument)
                    .withText("Top Left Watermark")
                    .ofSize(50)
                    .atPosition(WatermarkPosition.TOP_LEFT)
                    .usingMethod(WatermarkMethod.DRAW)
                    .inColor(Color.BLACK)
                    .withDpi(300f)
                .and()
                    .withText("Center Watermark")
                    .ofSize(150)
                    .usingMethod(WatermarkMethod.DRAW)
                    .withTrademark()
                    .rotate(325)
                    .atPosition(WatermarkPosition.CENTER)
                    .inColor(Color.BLUE)
                .apply()

        // Then
        assertNotNull(result, "The resulting byte array should not be null")
        assertTrue(result.isNotEmpty(), "The resulting byte array should not be empty")
        //outputFile(result, "DrawPlainPdf.pdf")
    }

    @Test
    @Throws(IOException::class)
    fun `given Plain Pdf when Overlay Method then Make Watermarked Pdf`() {
        // When
        val result = WatermarkService.create()
                .watermark(plainDocument)
                .withText("Sample Watermark")
                .ofSize(20)
                .usingMethod(WatermarkMethod.OVERLAY) // Overlay mode isn't resource-consuming, so a thread pool isn't necessary.
                .atPosition(WatermarkPosition.TOP_RIGHT)
                .withTrademark()
                .inColor(Color.YELLOW)
                .apply()

        // Then
        assertNotNull(result, "The resulting byte array should not be null")
        assertTrue(result.isNotEmpty(), "The resulting byte array should not be empty")
        //outputFile(result, "OverlayPlainPdf.pdf")
    }

    @Test
    @Throws(IOException::class)
    fun `given Landscape Pdf when Draw Method then Make Watermarked Pdf`() {
        // When
        val result = WatermarkService.create(
                Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())
        )
                .watermark(landscapeDocument)
                .withText("Sample Watermark")
                .atPosition(WatermarkPosition.CENTER)
                .usingMethod(WatermarkMethod.DRAW)
                .inColor(Color.BLUE)
                .withDpi(150f)
                .apply()

        // Then
        assertNotNull(result, "The resulting byte array should not be null")
        assertTrue(result.isNotEmpty(), "The resulting byte array should not be empty")
        //outputFile(result, "DrawLandscapePdf.pdf")
    }

    @Test
    @Throws(IOException::class)
    fun `given Landscape Pdf when Overlay Method then Make Watermarked Pdf`() {
        // When
        val result = WatermarkService.create()
                .watermark(landscapeDocument)
                .withText("Sample Watermark")
                .ofSize(30)
                .usingMethod(WatermarkMethod.OVERLAY) // Overlay mode isn't resource-consuming, so a thread pool isn't necessary.
                .atPosition(WatermarkPosition.BOTTOM_LEFT)
                .withTrademark()
                .inColor(Color.GREEN)
                .apply()

        // Then
        assertNotNull(result, "The resulting byte array should not be null")
        assertTrue(result.isNotEmpty(), "The resulting byte array should not be empty")
        //outputFile(result, "OverlayLandscapePdf.pdf")
    }

    private fun outputFile(result: ByteArray, filename: String) {
        val outputFile = File(filename)
        Files.write(outputFile.toPath(), result)
    }
}