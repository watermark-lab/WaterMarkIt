package com.markit

import com.markit.api.WatermarkService
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.common.PDRectangle
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.nio.file.Files
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class PdfImageWatermarkTest {
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
    fun `given Pdf when Image Watermark with 180 Degree Rotation then Apply Watermark`() {
        // When
        val result = WatermarkService.imageBasedWatermarker()
            .watermark(document)
            .withImage(readFileFromClasspathAsBytes("logo.png")).size(25)
            .rotation(180)
            .position(com.markit.api.WatermarkPosition.TILED)
            .apply()

        // Then
        assertNotNull(result, "The resulting byte array should not be null")
        assertTrue(result.isNotEmpty(), "The resulting byte array should not be empty")
        //outputFile(result, "ImageBasedWatermarkRotation.pdf")
    }

    @Test
    @Throws(IOException::class)
    fun `given Pdf when Image Watermark and DPI then Apply Watermark`() {
        // When
        val result = WatermarkService.imageBasedWatermarker()
            .watermark(document)
            .withImage(readFileFromClasspathAsBytes("logo.png")).size(25)
            .dpi(100f)
            .position(com.markit.api.WatermarkPosition.TILED)
            .apply()

        // Then
        assertNotNull(result, "The resulting byte array should not be null")
        assertTrue(result.isNotEmpty(), "The resulting byte array should not be empty")
        //outputFile(result, "ImageBasedWatermarkDPI.pdf")
    }

    @Test
    @Throws(IOException::class)
    fun `given Pdf when Image Watermark and Adjust then Apply Watermark`() {
        // When
        val result = WatermarkService.imageBasedWatermarker()
            .watermark(document)
            .withImage(readFileFromClasspathAsBytes("logo.png")).size(25)
            .position(com.markit.api.WatermarkPosition.TILED)
            .adjust(50, 50)
            .apply()

        // Then
        assertNotNull(result, "The resulting byte array should not be null")
        assertTrue(result.isNotEmpty(), "The resulting byte array should not be empty")
        //outputFile(result, "ImageBasedWatermarkAdjust.pdf")
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