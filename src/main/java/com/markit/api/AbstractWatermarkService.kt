package com.markit.api

import com.markit.exceptions.ConvertBytesToBufferedImageException
import com.markit.exceptions.WatermarkingException
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

abstract class AbstractWatermarkService {
    private val logger: Log = LogFactory.getLog(AbstractWatermarkService::class.java)

    abstract fun apply(): ByteArray

    fun apply(directoryPath: String, fileName: String): Path {
        if (!File(directoryPath).isDirectory) {
            logger.error(
                String.format(
                    "The directory does not exist or is not a directory: %s",
                    directoryPath
                )
            )
            throw IllegalArgumentException("The directory does not exist or is not a directory.")
        }

        try {
            val file: ByteArray = apply()
            val filePath = Paths.get(directoryPath, fileName)

            return Files.write(filePath, file)
        } catch (e: IOException) {
            logger.error("Failed to watermark file", e)
            throw WatermarkingException("Error watermarking the file", e)
        } catch (e: ConvertBytesToBufferedImageException) {
            logger.error("Failed to convert bytes to buffered image", e)
            throw WatermarkingException("Error converting bytes to buffered image", e)
        }
    }
}