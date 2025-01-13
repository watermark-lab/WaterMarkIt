package com.markit

import com.markit.api.WatermarkPosition
import com.markit.api.WatermarkService
import com.markit.api.WatermarkingMethod
import com.markit.exceptions.EmptyWatermarkObjectException
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.common.PDRectangle
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.IOException

class EmptyTextWatermarkExceptionTest {
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
    fun `given empty withText when draw method then throw exception`() {
        assertThrows<EmptyWatermarkObjectException> {
            WatermarkService.create()
                .watermarkPDF(document)
                .withText("")
                    .watermark()
                        .position(WatermarkPosition.CENTER)
                        .method(WatermarkingMethod.DRAW)
                .apply()
        }
    }
}