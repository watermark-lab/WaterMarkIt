package com.markit.pdf;

import com.markit.core.WatermarkAttributes;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.IOException;
import java.util.List;

/**
 * @author Oleg Cheban
 * @since 1.0
 */
@FunctionalInterface
interface PdfWatermarkHandler {
    void apply(PDDocument document, List<WatermarkAttributes> attributes) throws IOException;
}
