package com.markit.pdf

import org.apache.pdfbox.pdmodel.PDDocument
import org.junit.jupiter.api.AfterEach
import java.io.File
import java.io.InputStream
import java.nio.file.Files

abstract class BasePdfWatermarkTest {

    protected lateinit var document: PDDocument

    abstract fun initDocument();

    @AfterEach
    fun closeDocument() {
        if (::document.isInitialized) {
            document.close()
        }
    }

    protected fun readFileFromClasspathAsBytes(fileName: String): ByteArray? {
        val classLoader = Thread.currentThread().contextClassLoader
        val inputStream: InputStream? = classLoader.getResourceAsStream(fileName)
        return inputStream?.readBytes()
    }

    protected fun outputFile(result: ByteArray, filename: String) {
        val outputFile = File(filename)
        Files.write(outputFile.toPath(), result)
    }
}