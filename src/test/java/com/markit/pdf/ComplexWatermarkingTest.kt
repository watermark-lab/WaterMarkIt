package com.markit.pdf

import com.markit.utils.FileUtils
import com.markit.api.Font
import com.markit.api.positioning.WatermarkPosition
import com.markit.api.WatermarkService
import com.markit.api.WatermarkingMethod
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.awt.Color
import java.io.IOException
import java.time.LocalDateTime
import java.util.concurrent.Executors
import kotlin.test.assertTrue
import kotlin.test.assertNotNull

class ComplexWatermarkingTest : WatermarkPdfTest() {
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
        val watermarkText = "WaterMarkIt"
        val timestampText = LocalDateTime.now().toString()
        
        val result = WatermarkService.create(
                Executors.newFixedThreadPool(
                        Runtime.getRuntime().availableProcessors()
                )
        )
            .watermarkPDF(document)
                .withImage(FileUtils.readFileFromClasspathAsBytes("logo.png"))
                .position(WatermarkPosition.CENTER).end()
                .dpi(130)
                .opacity(20)
                .size(50)
            .and()
                .withText(watermarkText)
                    .addTrademark()
                    .bold()
                    .font(Font.ARIAL)
                    .color(Color.BLUE).end()
                .position(WatermarkPosition.TILED)
                    .horizontalSpacing(10).end()
                .opacity(10)
                .method(WatermarkingMethod.OVERLAY)
                .rotation(25)
                .size(55)
            .and()
                .withText(timestampText).end()
                .method(WatermarkingMethod.OVERLAY)
                .position(WatermarkPosition.TOP_RIGHT)
                    .adjust(0, -10).end()
                .size(30)
            .apply()

        assertNotNull(result, "The resulting byte array should not be null")
        assertTrue(result.isNotEmpty(), "The resulting byte array should not be empty")
        assertTrue(validateWatermarkText(result, watermarkText));
        assertTrue(validatePageCount(result, 3));
        assertTrue(validateImageContent(result));
    }
}
