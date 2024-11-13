package com.markit

import com.markit.api.FileType
import com.markit.api.WatermarkPosition
import com.markit.api.WatermarkService
import org.junit.jupiter.api.Test
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import java.nio.file.Files
import javax.imageio.ImageIO
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@SuppressWarnings("deprecation")
class TextWatermarkerImageTest {
    @Test
    @Throws(IOException::class)
    fun `given jpeg file when draw method then make watermarked jpeg`() {
        // Given
        val file = createJpegFile("test.jpeg");

        // When
        val result = WatermarkService.createTextWatermarker()
                .watermark(file, FileType.JPEG)
                .withText("Sample Watermark")
                .ofSize(30)
                .atPosition(WatermarkPosition.CENTER)
                .apply()

        // Then
        assertNotNull(result, "The resulting byte array should not be null")
        assertTrue(result.isNotEmpty(), "The resulting byte array should not be empty")
        assertTrue(result.size > file.length(), "The resulting byte array should be bigger than initial one")
        //outputFile(result, "test.jpeg")
        file.delete();
    }

    fun createJpegFile(fileName: String, width: Int = 600, height: Int = 800): File {
        val image = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
        val g2d = image.createGraphics()
        g2d.color = Color.WHITE
        g2d.fillRect(0, 0, width, height)
        g2d.dispose()
        val file = File(fileName)
        ImageIO.write(image, "jpg", file)
        return file;
    }

    private fun outputFile(result: ByteArray, filename: String) {
        val outputFile = File(filename)
        Files.write(outputFile.toPath(), result)
    }
}