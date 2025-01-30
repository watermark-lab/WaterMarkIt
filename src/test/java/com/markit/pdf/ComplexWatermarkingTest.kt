package com.markit.pdf

import com.markit.api.WatermarkPosition
import com.markit.api.WatermarkService
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.awt.Color
import java.io.IOException
import java.time.LocalDateTime
import java.util.concurrent.Executors
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class ComplexWatermarkingTest : BasePdfWatermarkTest() {
    @BeforeEach
    override fun initDocument() {
        document = PDDocument().apply {
            addPage(PDPage())
            addPage(PDPage())
            addPage(PDPage())
        }
    }

    @Test
    @Throws(IOException::class)
    fun `given Multi-Page Pdf when Apply Several Watermarks then Make Watermarked Pdf`() {
        // When
        val result = WatermarkService.create(
                Executors.newFixedThreadPool(
                        Runtime.getRuntime().availableProcessors()
                )
        )
            .watermarkPDF(document)
                .withImage(readFileFromClasspathAsBytes("logo.png"))
                    .position(WatermarkPosition.CENTER).end()
                    .opacity(0.2f)
            .and()
                .withText("WaterMarkIt")
                    .addTrademark()
                    .color(Color.BLUE)
                    .watermark()
                        .size(110)
                        .position(WatermarkPosition.TILED)
                            .adjust(35, 0)
                            .end()
                        .opacity(0.1f)
                        .rotation(25)
            .and()
                .withText(LocalDateTime.now().toString())
                    .watermark()
                        .position(WatermarkPosition.TOP_RIGHT)
                            .adjust(0, -30).end()
                        .size(50)
            .apply()

        // Then
        assertNotNull(result, "The resulting byte array should not be null")
        assertTrue(result.isNotEmpty(), "The resulting byte array should not be empty")
        //outputFile(result, "ImageBasedWatermark.pdf")
    }
}
