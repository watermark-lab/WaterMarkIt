package com.markit.services;

import com.markit.services.impl.FileType;
import com.markit.services.impl.WatermarkAPI;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.awt.*;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.Executor;

/**
 * Fluent API Watermark Service for applying watermarks to different file types.
 *
 * @author Oleg Cheban
 * @since 1.0
 */
public interface WatermarkService {

    /**
     * Sets the source file to be watermarked.
     *
     * @param sourceImageBytes The byte array representing the source file.
     * @return The current instance of {@code WatermarkService} for fluent API chaining.
     */
    WatermarkService file(byte[] sourceImageBytes);
    WatermarkService file(PDDocument pdDocument) throws IOException;

    /**
     * Specifies the type of file being processed (e.g., PDF, image).
     *
     * @param fileType The type of the file.
     * @see FileType
     */
    WatermarkService fileType(FileType fileType);

    /**
     * Sets the text to be used as the watermark.
     *
     * @param watermarkText The text to be used as the watermark.
     */
    WatermarkService watermarkText(String watermarkText);

    /**
     * Specifies the resolution for the watermark.
     *
     * @param dpi The resolution in DPI.
     */
    WatermarkService dpi(float dpi);

    /**
     * Apply sync mode (multipage PDF)
     */
    WatermarkService sync();

    /**
     * Sets the color of the watermark.
     */
    WatermarkService color(Color color);

    /**
     * Add a trademark symbol
     */
    WatermarkService trademark();

    /**
     * Sets the service used to watermark images.
     *
     * @param imageWatermarker The service for watermarking images.
     */
    WatermarkService setWatermarkImage(ImageWatermarker imageWatermarker);

    /**
     * Sets the service used to watermark PDF page.
     *
     * @param pdfWatermarker The service for watermarking PDF documents.
     */
    WatermarkService setWatermarkPdf(PdfWatermarker pdfWatermarker);

    /**
     * Sets the service used to watermark PDF files.
     *
     * @param watermarkPdfService The service for watermarking PDF files.
     */
    WatermarkService setWatermarkPdfService(WatermarkPdfService watermarkPdfService);

    /**
     * Applies the watermark to the specified file and returns the result.
     *
     * @return A byte array representing the watermarked file.
     */
    byte[] apply() throws IOException;

    /**
     * Creates a new instance of {@code WatermarkService} with a specified {@code Executor}.
     *
     * @param executor The {@code Executor} to be used for asynchronous operations.
     * @return A new instance of {@code WatermarkService}.
     * @throws NullPointerException If {@code executor} is {@code null}.
     */
    static WatermarkService create(Executor executor) {
        Objects.requireNonNull(executor, "executor is required");
        return new WatermarkAPI(executor);
    }

    /**
     * Creates a new instance of {@code WatermarkService} with default settings.
     *
     * @return A new instance of {@code WatermarkService}.
     */
    static WatermarkService create() {
        return new WatermarkAPI();
    }
}

