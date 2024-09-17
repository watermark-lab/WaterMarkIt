package com.markit.services;

import com.markit.services.impl.WatermarkMethod;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.awt.*;
import java.io.IOException;

/**
 * An interface for adding watermarks to a PDF page. ({@link WatermarkMethod#OVERLAY method}
 *
 * @author Oleg Cheban
 * @since 1.0
 */
public interface PdfWatermarkOverlayService {
    /**
     * Overlay a text watermark to a specific page of a PDF document.
     *
     * @param document      The PDF document to which the watermark will be applied.
     * @param pageIndex     The index of the page to be watermarked (zero-based).
     * @param watermarkText The text that will be used as the watermark on the specified page.
     * @param trademark     Add a trademark symbol.
     */
    void watermark(PDDocument document, int pageIndex, String watermarkText, Color watermarkColor, Boolean trademark) throws IOException;
}
