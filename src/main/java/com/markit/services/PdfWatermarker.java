package com.markit.services;


import com.markit.services.impl.WatermarkMethod;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import java.awt.*;
import java.io.IOException;

/**
 * An interface for adding watermarks to a PDF page. ({@link WatermarkMethod#DRAW method}
 *
 * @author Oleg Cheban
 * @since 1.0
 */
public interface PdfWatermarker {

    /**
     * Draw a text watermark to a specific page of a PDF document.
     *
     * @param document      The PDF document to which the watermark will be applied.
     * @param pdfRenderer   The renderer used to render the PDF page for watermarking.
     * @param pageIndex     The index of the page to be watermarked (zero-based).
     * @param dpi           The resolution at which the page is rendered for watermarking.
     * @param watermarkText The text that will be used as the watermark on the specified page.
     * @param trademark     Add a trademark symbol.
     */
    void watermark(PDDocument document, PDFRenderer pdfRenderer, int pageIndex, float dpi, String watermarkText, Color watermarkColor, Boolean trademark) throws IOException;
}
