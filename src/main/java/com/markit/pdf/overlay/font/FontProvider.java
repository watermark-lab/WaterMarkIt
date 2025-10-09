package com.markit.pdf.overlay.font;

import com.markit.api.WatermarkAttributes;
import com.markit.servicelocator.Prioritizable;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.font.PDFont;

import java.io.IOException;

/**
 * Interface for providing fonts based on watermark attributes.
 * Implementations can handle different font types, languages, or custom requirements.
 */
public interface FontProvider extends Prioritizable {

    /**
     * Loads and returns the appropriate font for the given attributes
     *
     * @param document the PDF document to load the font into
     * @return the loaded font
     * @throws IOException if font loading fails
     */
    PDFont loadFont(PDDocument document, WatermarkAttributes attributes) throws IOException;

    /**
     * Indicates whether this provider can handle the given attributes
     *
     * @param attributes the watermark attributes
     * @return true if this provider can handle the attributes
     */
    boolean canHandle(WatermarkAttributes attributes);

}
