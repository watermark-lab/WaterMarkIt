package com.markit.pdf

import com.markit.api.WatermarkingMethod
import com.markit.api.WatermarkPosition
import com.markit.api.WatermarkService
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.common.PDRectangle
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.IOException
import kotlin.test.assertNotNull
import kotlin.test.assertTrue


class PdfPageRotationTextBasedWatermarkTest : BasePdfWatermarkTest() {
    @BeforeEach
    override fun initDocument() {
        document = PDDocument().apply {
            addPage(PDPage(PDRectangle.A4).apply { rotation = 0 })
            addPage(PDPage(PDRectangle.A4).apply { rotation = 90 })
            addPage(PDPage(PDRectangle.A4).apply { rotation = 180 })
            addPage(PDPage(PDRectangle.A4).apply { rotation = 270 })
        }
    }

    @Test
    @Throws(IOException::class)
    fun `given Pdf with 0, 90, 180 and 270 degrees page rotation when Draw Method then Make Watermarked Pdf`() {
        // When
        val result = WatermarkService.create()
            .watermarkPDF(document)
            .withText("Sample Watermark")
                .end()
                    .size(50)
                    .position(WatermarkPosition.CENTER).end()
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
                .end()
                    .size(50)
                    .position(WatermarkPosition.CENTER).end()
                    .method(WatermarkingMethod.OVERLAY)
            .apply()

        // Then
        assertNotNull(result, "The resulting byte array should not be null")
        assertTrue(result.isNotEmpty(), "The resulting byte array should not be empty")
        //outputFile(result, "PDFwithDifferentPagesRotatedOverlay.pdf")
    }
}