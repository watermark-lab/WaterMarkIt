package com.markit.pdf.overlay;

import com.markit.core.WatermarkAttributes;
import com.markit.core.WatermarkingMethod;
import com.markit.servicelocator.Prioritizable;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.IOException;
import java.util.List;

/**
 * An interface for adding watermarks to a PDF page. ({@link WatermarkingMethod#OVERLAY method}
 *
 * @author Oleg Cheban
 * @since 1.0
 */
public interface OverlayPdfWatermarker extends Prioritizable {
    /**
     * Overlay a text watermark to a specific page of a PDF document.
     *
     * @param document The PDF document to which the watermark will be applied.
     * @param pageIndex The index of the page to be watermarked (zero-based).
     * @param attrs The attributes of watermark
     */
    void watermark(PDDocument document, int pageIndex, List<WatermarkAttributes> attrs) throws IOException;
}
