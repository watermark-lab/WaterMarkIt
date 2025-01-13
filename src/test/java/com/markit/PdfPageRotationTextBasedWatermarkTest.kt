package com.markit

import com.markit.api.WatermarkingMethod
import com.markit.api.WatermarkPosition
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
import kotlin.test.assertNotNull
import kotlin.test.assertTrue


class PdfPageRotationTextBasedWatermarkTest {
    private lateinit var document: PDDocument

    @BeforeEach
    fun initDocument() {
        document = PDDocument().apply {
            addPage(PDPage(PDRectangle.A4).apply { rotation = 0 })
            addPage(PDPage(PDRectangle.A4).apply { rotation = 90 })
            addPage(PDPage(PDRectangle.A4).apply { rotation = 180 })
            addPage(PDPage(PDRectangle.A4).apply { rotation = 270 })
        }
    }

    @AfterEach
    fun close() {
        document.close()
    }

    @Test
    @Throws(IOException::class)
    fun `given Pdf with 0, 90, 180 and 270 degrees page rotation when Draw Method then Make Watermarked Pdf`() {
        // When
        val result = WatermarkService.create()
            .watermarkPDF(document)
            .withText("Sample Watermark")
                .watermark()
                    .size(50)
                    .position(WatermarkPosition.CENTER)
                    .method(WatermarkingMethod.DRAW)
            .apply()

        // Then
        assertNotNull(result, "The resulting byte array should not be null")
        assertTrue(result.isNotEmpty(), "The resulting byte array should not be empty")
        //outputFile(result, "PDFwithDifferentPagesRotatedDraw.pdf")
    }

    @Test
    @Throws(IOException::class)
    fun `given Pdf with 0, 90, 180 and 270 degrees page rotation when Overlay Method then Make Watermarked Pdf`() {
        // When
        val result = WatermarkService.create()
            .watermarkPDF(document)
            .withText("Sample Watermark")
                .watermark()
                    .size(50)
                    .position(WatermarkPosition.CENTER)
                    .method(WatermarkingMethod.OVERLAY)
            .apply()

        // Then
        assertNotNull(result, "The resulting byte array should not be null")
        assertTrue(result.isNotEmpty(), "The resulting byte array should not be empty")
        //outputFile(result, "PDFwithDifferentPagesRotatedOverlay.pdf")
    }


    private fun outputFile(result: ByteArray, filename: String) {
        val outputFile = File(filename)
        Files.write(outputFile.toPath(), result)
    }
}