package com.markit.pdf;

import com.markit.api.WatermarkAttributes;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.IOException;
import java.util.List;

/**
 * @author Oleg Cheban
 * @since 1.0
 */
@FunctionalInterface
interface PdfWatermarkProcessor {
    void apply(PDDocument document, List<WatermarkAttributes> attributes) throws IOException;
}
