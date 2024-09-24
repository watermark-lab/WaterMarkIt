package com.markit.services;

import com.markit.services.impl.FileType;
import com.markit.services.impl.WatermarkAPI;
import com.markit.services.impl.WatermarkMethod;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.awt.*;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.Executor;

/**
 * Watermark Service for applying watermarks to different file types.
 *
 * @author Oleg Cheban
 * @since 1.0
 */
public interface WatermarkService {
    interface File {
        /**
         * Sets the source file to be watermarked.
         *
         * @param fileBytes The byte array representing the source file.
         * @param fileType The type of the file
         * @see FileType
         */
        Watermark file(byte[] fileBytes, FileType fileType);
        Watermark file(PDDocument document, FileType fileType) throws IOException;
    }

    interface Watermark {

        /**
         * Sets the text to be used as the watermark.
         *
         * @param text The text to be used as the watermark.
         */
        Watermark watermarkText(String text);

        /**
         * Defines the method for adding a watermark.
         *
         * @param watermarkMethod
         * @see WatermarkMethod
         */
        Watermark watermarkMethod(WatermarkMethod watermarkMethod);

        /**
         * Sets the color of the watermark.
         * @param color
         * @see Color
         */
        Watermark color(Color color);

        /**
         * Specifies the resolution for the watermark.
         *
         * @param dpi The resolution in DPI.
         */
        Watermark dpi(float dpi);

        /**
         * Add a trademark symbol
         */
        Watermark trademark();

        /**
         * Apply sync mode
         */
        Watermark sync();

        /**
         * Applies the watermark to the specified file and returns the result.
         *
         * @return A byte array representing the watermarked file.
         */
        byte[] apply() throws IOException;
    }

    static File create() {
        return new WatermarkAPI();
    }

    /**
     * Creates a new instance of {@code WatermarkService} with a specified {@code Executor}.
     *
     * @param executor The {@code Executor} to be used for asynchronous operations.
     * @return A new instance of {@code WatermarkService}.
     * @throws NullPointerException If {@code executor} is {@code null}.
     */
    static File create(Executor executor) {
        Objects.requireNonNull(executor, "executor is required");
        return new WatermarkAPI(executor);
    }

    static File create(Executor exr, ImageWatermarker i, PdfWatermarker d, OverlayPdfWatermarker o, WatermarkPdfService s) {
        nullCheck(i, d, o, s);
        Objects.requireNonNull(exr, "Executor is required");
        return new WatermarkAPI(exr, i, d, o, s);
    }

    static File create(ImageWatermarker i, PdfWatermarker d, OverlayPdfWatermarker o, WatermarkPdfService s) {
        nullCheck(i, d, o, s);
        return new WatermarkAPI(i, d, o, s);
    }

    static void nullCheck(ImageWatermarker i, PdfWatermarker d, OverlayPdfWatermarker o, WatermarkPdfService s){
        Objects.requireNonNull(i, "ImageWatermarker is required");
        Objects.requireNonNull(d, "PdfWatermarkDrawService is required");
        Objects.requireNonNull(o, "PdfWatermarkOverlayService is required");
        Objects.requireNonNull(s, "WatermarkPdfService is required");
    }
}
