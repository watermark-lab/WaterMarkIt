package com.markit.core.formats.pdf;

import com.markit.core.builders.TextBasedWatermarkBuilder;
import com.markit.core.WatermarkingMethod;
import com.markit.core.builders.WatermarkBuilder;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.function.Predicate;

/**
 * The Watermark Service for applying watermarks to PDFs
 *
 * @author Oleg Cheban
 * @since 1.3.0
 */
public interface WatermarkPDFService {

    /**
     * Text-based watermarking method
     *
     * @param text The text for the watermark
     */
    TextBasedWatermarkBuilder<WatermarkPDFBuilder> withText(String text);

    /**
     * Image-based watermarking method
     *
     * @param image the Byte array representation of the image
     */
    WatermarkPDFBuilder withImage(byte[] image);

    /**
     * Image-based watermarking method
     *
     * @param image the BufferedImage representation of the image
     */
    WatermarkPDFBuilder withImage(BufferedImage image);

    /**
     * Image-based watermarking method
     *
     * @param image the File object representing the image
     */
    WatermarkPDFBuilder withImage(File image);

    /**
     * The PDFs watermarking builder
     */
    interface WatermarkPDFBuilder extends WatermarkBuilder<WatermarkPDFService, WatermarkPDFBuilder> {

        /**
         * The watermarking method (default is DRAW)
         *
         * @param watermarkingMethod The method to use for watermarking
         * @see WatermarkingMethod
         */
        WatermarkPDFBuilder method(WatermarkingMethod watermarkingMethod);

        /**
         * The DPI for PDF
         */
        WatermarkPDFBuilder dpi(int dpi);

        /**
         * Filters documents to determine which should receive the watermark.
         *
         * @param predicate A condition that takes a PDDocument as input and returns true/false
         */
        WatermarkPDFBuilder documentFilter(Predicate<PDDocument> predicate);

        /**
         * Filters pages to determine which should receive the watermark.
         *
         * @param predicate A condition that takes a page number as input and returns true/false (the indexes starts from 0)
         */
        WatermarkPDFBuilder pageFilter(Predicate<Integer> predicate);
    }
}
