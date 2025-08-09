package com.markit.pdf

import com.markit.api.positioning.WatermarkPosition
import com.markit.api.WatermarkService
import com.markit.api.WatermarkingMethod
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.common.PDRectangle
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.IOException

class EmptyTextWatermarkExceptionTest : BasePdfWatermarkTest() {
    @BeforeEach
    override fun initDocument() {
        document = PDDocument().apply {
            addPage(PDPage(PDRectangle.A4))
        }
    }

    @Test
    @Throws(IOException::class)
    fun `given empty withText when draw method then throw exception`() {
        assertThrows<IllegalArgumentException> {
            WatermarkService.create()
                .watermarkPDF(document)
                    .withText("")
                        .end()
                            .position(WatermarkPosition.CENTER).end()
                            .method(WatermarkingMethod.DRAW)
                .apply()
        }
    }
}