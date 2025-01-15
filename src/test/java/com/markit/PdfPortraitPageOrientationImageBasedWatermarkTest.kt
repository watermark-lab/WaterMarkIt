package com.markit

import com.markit.api.WatermarkPosition
import com.markit.api.WatermarkService
import com.markit.api.WatermarkingMethod
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.awt.Color
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.nio.file.Files
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.concurrent.Executors
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class PdfPortraitPageOrientationImageBasedWatermarkTest {
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
    fun `given Portrait Pdf when Draw Method is Used then Make Image-Based Watermarked Pdf`() {
        // When
        val result = WatermarkService.create(
                Executors.newFixedThreadPool(
                        Runtime.getRuntime().availableProcessors()
                )
        )
            .watermarkPDF(document)
                .withImage(readFileFromClasspathAsBytes("logo.png"))
                    .position(WatermarkPosition.CENTER)
                    .opacity(0.2f)
            .and()
                .withText("WaterMarkIt")
                    .addTrademark()
                    .color(Color.BLUE)
                    .watermark()
                        .size(110)
                        .position(WatermarkPosition.TILED)
                            .adjust(35, 0)
                        .opacity(0.1f)
                        .rotation(25)
            .and()
                .withText(LocalDateTime.now().toString())
                    .watermark()
                        .position(WatermarkPosition.TOP_RIGHT)
                            .adjust(0, -30)
                        .size(50)
            .apply()

        // Then
        assertNotNull(result, "The resulting byte array should not be null")
        assertTrue(result.isNotEmpty(), "The resulting byte array should not be empty")
        //outputFile(result, "ImageBasedWatermark.pdf")
    }

    fun readFileFromClasspathAsBytes(fileName: String): ByteArray? {
        val classLoader = Thread.currentThread().contextClassLoader
        val inputStream: InputStream? = classLoader.getResourceAsStream(fileName)
        return inputStream?.readBytes()
    }

    private fun outputFile(result: ByteArray, filename: String) {
        val outputFile = File(filename)
        Files.write(outputFile.toPath(), result)
    }
}
