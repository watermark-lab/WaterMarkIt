package com.markit.pdf;

import com.markit.api.WatermarkAttributes;
import com.markit.api.WatermarkMethod;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import java.io.IOException;
import java.util.List;

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
     * @param document The PDF document to which the watermark will be applied.
     * @param pdfRenderer The renderer used to render the PDF page for watermarking.
     * @param pageIndex The index of the page to be watermarked (zero-based).
     * @param attrs The attributes of watermark
     */
    void watermark(PDDocument document, PDFRenderer pdfRenderer, int pageIndex, List<WatermarkAttributes> attrs) throws IOException;
}
