package com.markit.api.formats.pdf;

import com.markit.api.WatermarkingMethod;
import com.markit.api.builders.VisualWatermarkBuilder;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.util.function.Predicate;

/**
 * Configuration stage for a PDF watermark.
 *
 * @author Oleg Cheban
 * @since 1.3.0
 */
public interface WatermarkPDFBuilder
        extends VisualWatermarkBuilder<PdfWatermarkContentStep, WatermarkPDFBuilder> {

    /**
     * Sets the watermarking method. The default method is {@link WatermarkingMethod#DRAW}.
     *
     * @param watermarkingMethod the watermarking method
     * @return this configuration stage
     */
    WatermarkPDFBuilder method(WatermarkingMethod watermarkingMethod);

    /**
     * Sets the DPI used while watermarking the PDF.
     *
     * @param dpi the DPI value
     * @return this configuration stage
     */
    WatermarkPDFBuilder dpi(int dpi);

    /**
     * Filters documents to determine which should receive the watermark.
     *
     * @param predicate a condition evaluated against the document
     * @return this configuration stage
     */
    WatermarkPDFBuilder documentFilter(Predicate<PDDocument> predicate);

    /**
     * Filters pages to determine which should receive the watermark.
     *
     * @param predicate a condition evaluated against a zero-based page index
     * @return this configuration stage
     */
    WatermarkPDFBuilder pageFilter(Predicate<Integer> predicate);
}
