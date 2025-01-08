package com.markit

import com.markit.api.WatermarkPosition
import com.markit.api.WatermarkService
import com.markit.api.WatermarkingMethod
import com.markit.exceptions.EmptyWatermarkTextException
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.common.PDRectangle
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.PrintStream


class EmptyTextWatermarkExceptionTest {
    private lateinit var document: PDDocument
    private val originalErr = System.err
    private lateinit var errContent: ByteArrayOutputStream

    @BeforeEach
    fun initDocument() {
        document = PDDocument().apply {
            addPage(PDPage(PDRectangle.A4))
        }
        // Capture log output for verification
        errContent = ByteArrayOutputStream()
        System.setErr(PrintStream(errContent))
    }

    @AfterEach
    fun close() {
        document.close()
        System.setErr(originalErr)
    }

    @Test
    @Throws(IOException::class)
    fun `given empty withText when draw method then throw exception`() {

        assertThrows<EmptyWatermarkTextException> {
            WatermarkService.textBasedWatermarker()
                .watermark(document)
                .withText("")
                .position(WatermarkPosition.CENTER)
                .method(WatermarkingMethod.DRAW)
                .apply()
        }
        // Verify log that contains the expected error log
        val loggedMessage = errContent.toString().trim()
        assert(loggedMessage.contains("the watermarking text is empty")) {
            "Expected log message not found. Logged: $loggedMessage"
        }

    }
}