package com.markit.pdf.draw;

import com.markit.api.WatermarkAttributes;
import com.markit.api.WatermarkingMethod;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.IOException;
import java.util.List;

/**
 * An interface for adding watermarks to a PDF page. ({@link WatermarkingMethod#DRAW method}
 *
 * @author Oleg Cheban
 * @since 1.0
 */
public interface DrawPdfWatermarker {

    /**
     * Draw a text watermark to a specific page of a PDF document.
     *
     * @param document The PDF document to which the watermark will be applied.
     * @param pageIndex The index of the page to be watermarked (zero-based).
     * @param attrs The attributes of watermark
     */
    void watermark(PDDocument document, int pageIndex, List<WatermarkAttributes> attrs) throws IOException;
}
