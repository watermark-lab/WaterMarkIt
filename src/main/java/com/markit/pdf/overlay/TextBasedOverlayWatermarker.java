package com.markit.pdf.overlay;

import com.markit.api.WatermarkAttributes;
import com.markit.servicelocator.Prioritizable;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;

import java.io.IOException;

/**
 * The interface for adding text-based watermarks
 *
 * @author Oleg Cheban
 * @since 1.3.5
 */
public interface TextBasedOverlayWatermarker extends Prioritizable {

    /**
     * Adds a watermark
     *
     * @param contentStream pdf page content stream
     * @param pdRectangle the page boundaries in default user space units (PDF points)
     * @param attr the watermark attributes
     */
    void overlay(PDDocument document, PDPageContentStream contentStream, PDRectangle pdRectangle, WatermarkAttributes attr) throws IOException;
}
