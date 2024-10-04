package com.markit.pdf;

import com.markit.api.WatermarkAttributes;
import com.markit.api.WatermarkMethod;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.IOException;
import java.util.List;

/**
 * An interface for adding watermarks to a PDF page. ({@link WatermarkMethod#OVERLAY method}
 *
 * @author Oleg Cheban
 * @since 1.0
 */
public interface OverlayPdfWatermarker {
    /**
     * Overlay a text watermark to a specific page of a PDF document.
     *
     * @param document The PDF document to which the watermark will be applied.
     * @param pageIndex The index of the page to be watermarked (zero-based).
     * @param attrs The attributes of watermark
     */
    void watermark(PDDocument document, int pageIndex, List<WatermarkAttributes> attrs) throws IOException;
}
