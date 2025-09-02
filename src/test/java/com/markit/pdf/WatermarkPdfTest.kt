package com.markit.pdf

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject
import org.apache.pdfbox.text.PDFTextStripper
import org.junit.jupiter.api.AfterEach
import java.io.ByteArrayInputStream
import java.io.IOException

abstract class WatermarkPdfTest {

    protected lateinit var document: PDDocument

    abstract fun initDocument();

    @AfterEach
    fun closeDocument() {
        if (::document.isInitialized) {
            document.close()
        }
    }

    /**
     * Validates that a PDF byte array contains the expected watermark text
     */
    fun validateWatermarkText(pdfBytes: ByteArray, expectedText: String, ignoreCase: Boolean = true): Boolean {
        return try {
            val document = PDDocument.load(ByteArrayInputStream(pdfBytes))
            document.use { doc ->
                val textStripper = PDFTextStripper()
                textStripper.startPage = 1
                textStripper.endPage = doc.numberOfPages
                val extractedText = textStripper.getText(doc)
                extractedText.contains(expectedText, ignoreCase)
            }
        } catch (e: IOException) {
            false
        }
    }

    /**
     * Validates that a PDF has the expected number of pages
     */
    fun validatePageCount(pdfBytes: ByteArray, expectedPageCount: Int): Boolean {
        return try {
            val document = PDDocument.load(ByteArrayInputStream(pdfBytes))
            document.use { doc ->
                doc.numberOfPages == expectedPageCount
            }
        } catch (e: IOException) {
            false
        }
    }

    /**
     * Validates that a PDF contains image content (for image watermarks)
     */
    fun validateImageContent(pdfBytes: ByteArray): Boolean {
        return try {
            val document = PDDocument.load(ByteArrayInputStream(pdfBytes))
            document.use { doc ->
                doc.pages.any { page ->
                    page.resources?.xObjectNames?.any { name ->
                        page.resources.getXObject(name) is PDImageXObject
                    } ?: false
                }
            }
        } catch (e: IOException) {
            false
        }
    }
}