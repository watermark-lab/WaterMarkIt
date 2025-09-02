package com.markit.utils

import java.io.File
import java.io.InputStream
import java.nio.file.Files

object FileUtils {

    fun readFileFromClasspathAsBytes(fileName: String): ByteArray? {
        val classLoader = Thread.currentThread().contextClassLoader
        val inputStream: InputStream? = classLoader.getResourceAsStream(fileName)
        return inputStream?.readBytes()
    }

    fun outputFile(result: ByteArray, filename: String) {
        val outputFile = File(filename)
        Files.write(outputFile.toPath(), result)
    }
}
