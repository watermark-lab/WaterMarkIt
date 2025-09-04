package com.markit.pdf.overlay;

import com.markit.api.WatermarkAttributes;
import com.markit.servicelocator.Prioritizable;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.IOException;

/**
 * The interface for adding image-based watermarks
 *
 * @author Oleg Cheban
 * @since 1.3.5
 */
public interface ImageBasedOverlayWatermarker extends Prioritizable {

    /**
     * Adds a watermark
     *
     * @param contentStream pdf page content stream
     * @param imageXObject the image of watermark
     * @param pdRectangle the page boundaries in default user space units (PDF points)
     * @param attr the watermark attributes
     */
    void overlay(PDPageContentStream contentStream, PDImageXObject imageXObject, PDRectangle pdRectangle, WatermarkAttributes attr) throws IOException;
}
