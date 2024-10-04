package com.markit.pdf;

import com.markit.api.WatermarkAttributes;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * watermark pdf files
 *
 * @author Oleg Cheban
 * @since 1.0
 */
public interface WatermarkPdfService {
    /**
     * Adds a text watermark to a PDF file.
     *
     * @param sourceImageBytes The byte array representing the PDF file to which the watermark will be applied.
     * @param isAsyncMode If {@code true}, the watermarking process will be performed asynchronously.
     *                    If {@code false}, it will be performed synchronously.
     * @param attrs The attributes of watermark
     * @return A byte array representing the watermarked PDF file.
     */
    byte[] watermark(byte[] sourceImageBytes, boolean isAsyncMode, List<WatermarkAttributes> attrs) throws IOException;

    /**
     * Adds a text watermark to a PDF file.
     *
     * @param file The PDF file to which the watermark will be applied.
     * @param isAsyncMode If {@code true}, the watermarking process will be performed asynchronously.
     *                    If {@code false}, it will be performed synchronously.
     * @param attrs The attributes of watermark
     * @return A byte array representing the watermarked PDF file.
     */
    byte[] watermark(File file, boolean isAsyncMode, List<WatermarkAttributes> attrs) throws IOException;

    /**
     * Adds a text watermark to a PDF file.
     *
     * @param pdDocument The pdfbox pdf file representation to which the watermark will be applied.
     * @param isAsyncMode If {@code true}, the watermarking process will be performed asynchronously.
     *                    If {@code false}, it will be performed synchronously.
     * @param attrs The attributes of watermark
     * @return A byte array representing the watermarked PDF file.
     */
    byte[] watermark(PDDocument pdDocument, boolean isAsyncMode, List<WatermarkAttributes> attrs) throws IOException;
}
