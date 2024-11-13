package com.markit.pdf;

import com.markit.api.TextWatermarkAttributes;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.IOException;
import java.util.List;

/**
 * @author Oleg Cheban
 * @since 1.0
 */
@FunctionalInterface
interface PdfWatermarkHandler {
    void apply(PDDocument document, List<TextWatermarkAttributes> attributes) throws IOException;
}
