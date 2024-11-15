package com.markit

import com.markit.api.WatermarkMethod
import com.markit.api.WatermarkService
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.common.PDRectangle
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File
import java.io.IOException
import java.nio.file.Files
import kotlin.test.assertTrue

class PdfLandscapePageOrientationTextBasedWatermarkTest {
    private lateinit var landscapeDocument: PDDocument

    @BeforeEach
    fun initDocument() {
        landscapeDocument = PDDocument().apply {
            val landscapePage = PDPage(PDRectangle.A4).apply {
                mediaBox = PDRectangle(PDRectangle.A4.height, PDRectangle.A4.width)
            }
            addPage(landscapePage)
        }
    }

    @AfterEach
    fun close(){
        landscapeDocument.close()
    }

    @Test
    @Throws(IOException::class)
    fun `given Landscape Pdf when Draw Method then Make Watermarked Pdf`() {
        // When
        val result = WatermarkService.textBasedWatermarker()
                .watermark(landscapeDocument)
                .withText("Sample Watermark")
                .usingMethod(WatermarkMethod.DRAW)
                .apply()

        // Then
        assertTrue(result.isNotEmpty(), "The resulting byte array should not be empty")
        //outputFile(result, "DrawLandscapePdf.pdf")
    }

    @Test
    @Throws(IOException::class)
    fun `given Landscape Pdf when Overlay Method then Make Watermarked Pdf`() {
        // When
        val result = WatermarkService.textBasedWatermarker()
                .watermark(landscapeDocument)
                .withText("Sample Watermark").ofSize(50)
                .usingMethod(WatermarkMethod.OVERLAY)
                .apply()

        // Then
        assertTrue(result.isNotEmpty(), "The resulting byte array should not be empty")
        //outputFile(result, "OverlayLandscapePdf.pdf")
    }

    private fun outputFile(result: ByteArray, filename: String) {
        val outputFile = File(filename)
        Files.write(outputFile.toPath(), result)
    }
}