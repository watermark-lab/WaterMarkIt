package com.markit.pdf;

import com.markit.core.WatermarkAttributes;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.IOException;
import java.util.List;

/**
 * watermark pdf files
 *
 * @author Oleg Cheban
 * @since 1.0
 */
public interface WatermarkPdfService {

    /**
     * Adds a text watermark to a PDF file.
     *
     * @param pdDocument The pdfbox pdf file representation to which the watermark will be applied.
     * @param attrs The attributes of watermark
     * @return A byte array representing the watermarked PDF file.
     */
    byte[] watermark(PDDocument pdDocument, List<WatermarkAttributes> attrs) throws IOException;
}
