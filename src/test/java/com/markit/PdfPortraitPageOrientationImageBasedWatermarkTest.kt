package com.markit

import com.markit.api.WatermarkService
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.nio.file.Files
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
        val result = WatermarkService.imageBasedWatermarker(
                Executors.newFixedThreadPool(
                        Runtime.getRuntime().availableProcessors()
                )
        )
            .watermark(document)
            .withImage(readFileFromClasspathAsBytes("whatsapp.png"))
            .dpi(300f)
            .opacity(0.3f)
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