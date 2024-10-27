package com.markit

import com.markit.api.WatermarkMethod
import com.markit.api.WatermarkPosition
import com.markit.api.WatermarkService
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
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

class PdfPortraitPageOrientationTest {
    private lateinit var document: PDDocument

    @BeforeEach
    fun initDocument() {
        document = PDDocument().apply {
            addPage(PDPage())
            addPage(PDPage())
            addPage(PDPage())
        }
    }

    @AfterEach
    fun close(){
        document.close()
    }

    @Test
    @Throws(IOException::class)
    fun `given Portrait Pdf when Draw Method is Used then Make Watermarked Pdf`() {
        // When
        val result = WatermarkService.create(
                Executors.newFixedThreadPool(
                        Runtime.getRuntime().availableProcessors()
                )
        )
            .watermark(document)
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
                    .rotate(45)
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
    fun `given Portrait Pdf when Overlay Method then Make Watermarked Pdf`() {
        // When
        val result = WatermarkService.create()
                .watermark(document)
                .withText("Sample Watermark")
                .ofSize(40)
                .usingMethod(WatermarkMethod.OVERLAY) // Overlay mode isn't resource-consuming, so a thread pool isn't necessary.
                .atPosition(WatermarkPosition.CENTER)
                .rotate(45)
                .withTrademark()
                .withOpacity(0.2f)
                .inColor(Color.RED)
                .apply()

        // Then
        assertNotNull(result, "The resulting byte array should not be null")
        assertTrue(result.isNotEmpty(), "The resulting byte array should not be empty")
        //outputFile(result, "OverlayPlainPdf.pdf")
    }

    @Test
    @Throws(IOException::class)
    fun `given Portrait Pdf when Draw Method and TILED position is Used then Make Watermarked Pdf`() {
        // When
        val result = WatermarkService.create(
            Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors()
            )
        )
            .watermark(document)
            .withText("CISCO").ofSize(194)
            .usingMethod(WatermarkMethod.DRAW)
            .atPosition(WatermarkPosition.TILED)
            .inColor(Color.RED)
            .withOpacity(0.5f)
            .withDpi(300f)
            .apply()

        // Then
        assertNotNull(result, "The resulting byte array should not be null")
        assertTrue(result.isNotEmpty(), "The resulting byte array should not be empty")
        //outputFile(result, "tiled.pdf")
    }

    private fun outputFile(result: ByteArray, filename: String) {
        val outputFile = File(filename)
        Files.write(outputFile.toPath(), result)
    }
}